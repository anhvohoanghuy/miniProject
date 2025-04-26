package com.example.demo.view_model;

import jakarta.validation.constraints.NotNull;

import java.security.PublicKey;

public class ChangePassVM {
    @NotNull
    public String OldPassWord;
    @NotNull
    public String NewPassWord;
    @NotNull
    public String ComfirmPassWord;
}
