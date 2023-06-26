package com.bugsolver.auth.entity;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class TokenResponse {
    private String authToken;
    private String refreshToken;
    private Instant authTokenExpiresAt;
    private Instant refreshTokenExpiresAt;
}
