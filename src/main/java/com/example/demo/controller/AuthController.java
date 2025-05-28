package com.example.demo.controller;
import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.service.AuthService.IAuthService;
import com.example.demo.view_model.ChangePassVM;
import com.example.demo.view_model.LoginVM;
import com.example.demo.view_model.RegisterVM;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;
    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<ApiResponse<AuthResponse>>> login(@RequestBody @Valid LoginVM loginVm) {

        return authService.login(loginVm).thenApply(authResponse -> ResponseEntity.ok(ApiResponse.ok(authResponse)));
    }
    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<ApiResponse<User>>> register(@RequestBody @Valid RegisterVM registerVM){
        return authService.register(registerVM).thenApply(user-> ResponseEntity.ok(ApiResponse.ok(user)));
    }
    @PatchMapping("/{id}/changePassword")
    public  CompletableFuture<ResponseEntity<ApiResponse<Void>>> changePassWord(@PathVariable String id, @RequestBody @Valid ChangePassVM model){
        return authService.changePassWord(id,model).thenApply(s-> ResponseEntity.ok(ApiResponse.ok(s)));
    }
    @PostMapping("/logout")
    public CompletableFuture<ResponseEntity<ApiResponse<Void>>> logout(@RequestBody AuthResponse request){
        return  authService.logout(request).thenApply(s-> ResponseEntity.ok(ApiResponse.ok(s)));
    }
    @PostMapping("/refresh")
    public CompletableFuture<ResponseEntity<ApiResponse<Void>>> refresh(@RequestBody AuthResponse request){
        return authService.refresh(request).thenApply(s-> ResponseEntity.ok(ApiResponse.ok(s)));
    }
}
