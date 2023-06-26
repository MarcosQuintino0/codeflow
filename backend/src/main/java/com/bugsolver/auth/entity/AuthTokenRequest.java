package com.bugsolver.auth.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthTokenRequest {

    @NotBlank(message = "auth.auth-token.username-not-blank")
    private String username;

    @NotBlank(message = "auth.auth-token.password-not-blank")
    private String password;
}
