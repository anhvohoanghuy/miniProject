package com.example.demo.controller;

import com.example.demo.model.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.model.UserClaim;
import com.example.demo.service.JwtUtil;
import com.example.demo.view_model.ChangePassVM;
import com.example.demo.view_model.LoginVM;
import com.example.demo.repository.IUserClaimRepository;
import com.example.demo.repository.IUserRepository;
import com.example.demo.view_model.RegisterVM;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private IUserClaimRepository userClaimRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginVM loginVm) {
        User user= userRepository.findByUsername(loginVm.username);
        if(user==null){
            return ResponseEntity.badRequest().body("Invalid username");
        }
        if(!user.getPassword().equals(loginVm.password)){
            return ResponseEntity.badRequest().body("Invalid password");
        }
        List<UserClaim> userClaims= userClaimRepository.findByUserId(user.getId());
        //lấy claim value từ userClaims
        List<String> claimList= userClaims.stream().map(userClaim->userClaim.getClaim().getClaimValue()).toList();
        Map<String,Object> claims= new HashMap<>();
        claims.put("roles",claimList);
        String accessToken= jwtUtil.generateAccessToken(loginVm.username,claims);
        String refreshToken= jwtUtil.generateRefeshToken(loginVm.username);
        AuthResponse authResponse = new AuthResponse(accessToken,refreshToken,user.getId().toString());
        return ResponseEntity.ok(authResponse);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterVM registerVM){
        User user = userRepository.findByUsername(registerVM.username);
        if(user!=null){
            ResponseEntity.badRequest().body("UserName đã tồn tại");
        }if(registerVM.password.equals(registerVM.comfirmPassword)){
            ResponseEntity.badRequest().body("Mật khẩu không trùng nhau");
        }
        User newUser= new User();
        newUser.setEmail(registerVM.email);
        newUser.setUsername(registerVM.username);
        newUser.setPassword(registerVM.password);
        userRepository.save(newUser);
        User currentUser= userRepository.findByUsername(registerVM.username);
        if(currentUser ==null){
            return ResponseEntity.badRequest().body("Lỗi khi add role");
        }
        UserClaim userClaim = new UserClaim();
        userClaim.claimId=2;
        userClaim.userId=currentUser.getId();
        userClaimRepository.save(userClaim);
        return ResponseEntity.ok(newUser);
    }
    @PatchMapping("/{id}/changePassword")
    public  ResponseEntity <?> changePassWord(@PathVariable UUID id, @RequestBody @Valid ChangePassVM model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByUsername(userName);
        if(user==null){
            return ResponseEntity.badRequest().body("User not found");
        }
        if(!model.CurrentPassword.equals(user.getPassword())){
            return ResponseEntity.badRequest().body("Sai mật khẩu cũ");
        }
        user.setPassword(model.NewPassword);
        userRepository.save(user);
        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody AuthResponse request){
        String tokenId= jwtUtil.getRefreshTokenId(request.getRefreshToken());
        jwtUtil.deleteRefreshToken(tokenId);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout success");
    }
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody AuthResponse request){
        String tokenId= jwtUtil.getRefreshTokenId(request.getRefreshToken());
        if(jwtUtil.validateRefreshToken(tokenId)){
            String oldAccessToken = request.getAccessToken();
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
