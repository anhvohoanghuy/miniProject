package com.example.demo.controller;

import com.example.demo.model.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.model.UserClaim;
import com.example.demo.service.Interface.IAuthService;
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
    private final IAuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginVM loginVm) {
        return authService.login(loginVm);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterVM registerVM){
        return authService.register(registerVM);
    }
    @PatchMapping("/{id}/changePassword")
    public  ResponseEntity <?> changePassWord(@PathVariable String id, @RequestBody @Valid ChangePassVM model){
        return authService.changePassWord(model);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody AuthResponse request){
        return  authService.logout(request);
    }
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody AuthResponse request){
        return authService.refresh(request);
    }
}
