package com.example.demo.service.AuthService;

import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorEnum;
import com.example.demo.exception.WrongPasswordException;
import com.example.demo.dto.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.model.UserClaim;
import com.example.demo.repository.IUserClaimRepository;
import com.example.demo.repository.IUserRepository;
import com.example.demo.service.JwtService.JwtUtil;
import com.example.demo.view_model.ChangePassVM;
import com.example.demo.view_model.LoginVM;
import com.example.demo.view_model.RegisterVM;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final IUserClaimRepository userClaimRepository;


    @Override
    @Async
    public CompletableFuture<AuthResponse> login(LoginVM model) {
        return userRepository.findByUsername(model.username)
                .thenCompose(userOptional -> {
                    if (userOptional.isEmpty()) {
                        throw new UsernameNotFoundException("Không tìm thấy UserName: " + model.username);
                    }
                    User user = userOptional.get();

                    if (!user.getPassword().equals(model.password)) {
                        throw new WrongPasswordException("Sai mật khẩu");
                    }

                    List<UserClaim> userClaims = userClaimRepository.findByUserId(user.getId());
                    List<String> claimList = userClaims.stream().map(c -> c.getClaim().getClaimValue()).toList();

                    Map<String, Object> claims = new HashMap<>();
                    claims.put("roles", claimList);

                    CompletableFuture<String> accessTokenFuture = jwtUtil.generateAccessToken(user.getUsername(), claims);
                    CompletableFuture<String> refreshTokenFuture = jwtUtil.generateRefeshToken(user.getUsername());

                    return accessTokenFuture.thenCombine(refreshTokenFuture, (accessToken, refreshToken) ->
                            new AuthResponse(accessToken, refreshToken, user.getId()));
                });
    }

    @Override
    @Async
    public CompletableFuture<User> register(RegisterVM model) {
        CompletableFuture<Boolean> futureIsUsernameExist = userRepository.existsByUsername(model.username);
        CompletableFuture<Boolean> futureIsEmailExist = userRepository.existsByEmail(model.email);
        return futureIsUsernameExist.thenCombine(futureIsEmailExist, (isUsernameExist, isEmailExist) -> {
            if (isUsernameExist) {
                throw new CustomException(ErrorEnum.USERNAME_ALREADY_EXISTS);
            }
            if (isEmailExist) {
                throw new CustomException(ErrorEnum.Email_ALREADY_EXISTS);
            }
            User newUser = new User();
            newUser.setEmail(model.email);
            newUser.setUsername(model.username);
            newUser.setPassword(model.password);
            return newUser;
        }).thenApply(user -> SaveUser(user, 2));
    }

    @Transactional
    private User SaveUser(User user, int ClaimId) {
        userRepository.save(user);
        UserClaim userClaim = new UserClaim();
        userClaim.setClaimId(2);
        userClaim.setUserId(user.getId());
        userClaimRepository.save(userClaim);
        return user;
    }

    @Override
    @Async
    public CompletableFuture<String> changePassWord(String id, ChangePassVM model) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<User> user = userRepository.findById(id);
            if (user.isEmpty()) {
                throw new CustomException(ErrorEnum.USER_NOT_FOUND);
            }
            if (!model.CurrentPassword.equals(user.get().getPassword())) {
                throw new CustomException(ErrorEnum.WRONG_PASSWORD);
            }
            user.get().setPassword(model.NewPassword);
            userRepository.save(user.get());
            return "Password change success";
        });
    }

    @Override
    @Async
    public CompletableFuture<String> logout(AuthResponse model) {
        return CompletableFuture.supplyAsync(() -> {
            String tokenId = jwtUtil.getRefreshTokenId(model.getRefreshToken());
            jwtUtil.deleteRefreshToken(tokenId);
            SecurityContextHolder.clearContext();
            return "Logout success";
        });
    }

    @Override
    @Async
    public CompletableFuture<String> refresh(AuthResponse model) {
        String tokenId = jwtUtil.getRefreshTokenId(model.getRefreshToken());
        if (jwtUtil.validateRefreshToken(tokenId)) {
            String oldAccessToken = model.getAccessToken();
            String userName = jwtUtil.getUserName(oldAccessToken);
            Claims claims = jwtUtil.getClaimsFromToken(oldAccessToken);
            return jwtUtil.generateAccessToken(userName, claims);
        } else {
            throw new CustomException(ErrorEnum.INVALID_TOKEN);
        }
    }
}
