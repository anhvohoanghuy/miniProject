package com.example.demo.service.AuthService;

import com.example.demo.dto.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.view_model.ChangePassVM;
import com.example.demo.view_model.LoginVM;
import com.example.demo.view_model.RegisterVM;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

public interface IAuthService {
    CompletableFuture<AuthResponse> login(LoginVM model);
    CompletableFuture<User> register(RegisterVM model);
    CompletableFuture<String> changePassWord(String id,ChangePassVM model);
    CompletableFuture<String> logout(AuthResponse model);
    CompletableFuture<String> refresh(AuthResponse model);
}
