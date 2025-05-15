package com.example.demo.service;

import com.example.demo.model.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.model.UserClaim;
import com.example.demo.repository.IUserClaimRepository;
import com.example.demo.repository.IUserRepository;
import com.example.demo.service.Interface.IAuthService;
import com.example.demo.view_model.ChangePassVM;
import com.example.demo.view_model.LoginVM;
import com.example.demo.view_model.RegisterVM;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final IUserClaimRepository userClaimRepository;

    @Override
    public ResponseEntity<?> login(LoginVM model) {
        Optional<User> userOptional= userRepository.findByUsername(model.username);
        if(userOptional.isEmpty()){
            return ResponseEntity.badRequest().body("Invalid username");
        }
        User user=userOptional.get();
        if(!user.getPassword().equals(model.password)){
            return ResponseEntity.badRequest().body("Invalid password");
        }
        List<UserClaim> userClaims= userClaimRepository.findByUserId(user.getId());
        //lấy claim value từ userClaims
        List<String> claimList= userClaims.stream().map(userClaim->userClaim.getClaim().getClaimValue()).toList();
        Map<String,Object> claims= new HashMap<>();
        claims.put("roles",claimList);
        String accessToken= jwtUtil.generateAccessToken(model.username,claims);
        String refreshToken= jwtUtil.generateRefeshToken(model.username);
        AuthResponse authResponse = new AuthResponse(accessToken,refreshToken,user.getId().toString());
        return ResponseEntity.ok(authResponse);
    }

    @Override
    @Transactional
    public ResponseEntity<?> register(RegisterVM model) {
        Optional<User> user = userRepository.findByUsername(model.username);
        if(user.isPresent()){
            return ResponseEntity.badRequest().body("UserName đã tồn tại");
        }
        if(!model.password.equals(model.comfirmPassword)){
            return ResponseEntity.badRequest().body("Mật khẩu không trùng nhau");
        }
        User newUser= new User();
        newUser.setEmail(model.email);
        newUser.setUsername(model.username);
        newUser.setPassword(model.password);
        userRepository.save(newUser);
        Optional<User> currentUser= userRepository.findByUsername(model.username);
        if(currentUser.isEmpty()){
            return ResponseEntity.badRequest().body("Lỗi khi add role");
        }
        UserClaim userClaim = new UserClaim();
        userClaim.setClaimId(2);
        userClaim.setUserId(currentUser.get().getId());
        userClaimRepository.save(userClaim);
        return ResponseEntity.ok(newUser);
    }

    @Override
    public ResponseEntity<?> changePassWord(ChangePassVM model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> user = userRepository.findByUsername(userName);
        if(user.isEmpty()){
            return ResponseEntity.badRequest().body("User not found");
        }
        if(!model.CurrentPassword.equals(user.get().getPassword())){
            return ResponseEntity.badRequest().body("Sai mật khẩu cũ");
        }
        user.get().setPassword(model.NewPassword);
        userRepository.save(user.get());
        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }

    @Override
    public ResponseEntity<?> logout(AuthResponse model) {
        String tokenId= jwtUtil.getRefreshTokenId(model.getRefreshToken());
        jwtUtil.deleteRefreshToken(tokenId);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout success");
    }

    @Override
    public ResponseEntity<String> refresh(AuthResponse model) {
        String tokenId= jwtUtil.getRefreshTokenId(model.getRefreshToken());
        if(jwtUtil.validateRefreshToken(tokenId)){
            String oldAccessToken = model.getAccessToken();
            String userName = jwtUtil.getUserName(oldAccessToken);
            Claims claims = jwtUtil.getClaimsFromToken(oldAccessToken);
            String accessToken = jwtUtil.generateAccessToken(userName,claims);
            return ResponseEntity.ok(accessToken);
        }
        else {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }
}
