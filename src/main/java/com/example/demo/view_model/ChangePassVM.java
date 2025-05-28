package com.example.demo.view_model;

import jakarta.validation.constraints.NotNull;

import java.security.PublicKey;

public class ChangePassVM {
    @NotNull
    public String CurrentPassword;
    @NotNull
    public String NewPassword;
    @NotNull
    public String UserId;
}
