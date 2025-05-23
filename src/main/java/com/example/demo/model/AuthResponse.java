package com.example.demo.model;

import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter  @Setter @NoArgsConstructor @AllArgsConstructor
public class AuthResponse {
    private String AccessToken;
    private String RefreshToken;
    private String UserId;
}
