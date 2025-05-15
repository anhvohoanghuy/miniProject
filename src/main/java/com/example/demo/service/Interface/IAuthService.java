package com.example.demo.service.Interface;

import com.example.demo.model.AuthResponse;
import com.example.demo.view_model.ChangePassVM;
import com.example.demo.view_model.LoginVM;
import com.example.demo.view_model.RegisterVM;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    public ResponseEntity<?> login(LoginVM model);
    public ResponseEntity<?> register(RegisterVM model);
    public ResponseEntity<?> changePassWord(ChangePassVM model);
    public ResponseEntity<?> logout(AuthResponse model);
    public ResponseEntity<String> refresh(AuthResponse model);
}
