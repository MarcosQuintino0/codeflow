package com.bugsolver.auth.entity;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class RefreshTokenRequest {

    @NotBlank(message = "auth.refresh-token.bearer-token-not-blank")
    private String bearerToken;

    @NotBlank(message = "auth.refresh-token.refresh-token-not-blank")
    private String refreshToken;
}
