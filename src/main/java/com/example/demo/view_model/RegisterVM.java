package com.example.demo.view_model;

import jakarta.validation.constraints.NotNull;

public class RegisterVM {
    @NotNull
    public String username;
    @NotNull
    public String email;
    @NotNull
    public String password;
    @NotNull
    public String comfirmPassword;
}
