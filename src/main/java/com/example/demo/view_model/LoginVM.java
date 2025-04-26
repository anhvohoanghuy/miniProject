package com.example.demo.view_model;

import jakarta.validation.constraints.NotNull;

public class LoginVM {
    @NotNull
    public String username;
    @NotNull
    public String password;
}
