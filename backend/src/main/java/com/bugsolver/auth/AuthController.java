package com.bugsolver.auth;

import com.bugsolver.auth.entity.AuthTokenRequest;
import com.bugsolver.auth.entity.RefreshTokenRequest;
import com.bugsolver.auth.entity.TokenResponse;
import com.bugsolver.auth.service.AuthService;
import com.bugsolver.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthTokenRequest request){
        authService.authenticate(request);

        var authToken = jwtProvider.createAuthToken(request.getUsername());
        var refreshToken = jwtProvider.createRefreshToken(request.getUsername());

        var authResponse = TokenResponse.builder()
                .authToken(authToken)
                .refreshToken(refreshToken)
                .authTokenExpiresAt(
                        jwtProvider.decodeToken(authToken).getExpiresAtAsInstant()
                )
                .refreshTokenExpiresAt(
                        jwtProvider.decodeToken(authToken).getExpiresAtAsInstant()
                )
                .build();

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request){
        String newAuthToken = jwtProvider.refreshAuthToken(request.getRefreshToken());

        var authResponse = TokenResponse.builder()
                .authToken(newAuthToken)
                .refreshToken(request.getRefreshToken())
                .authTokenExpiresAt(
                        jwtProvider.decodeToken(newAuthToken).getExpiresAtAsInstant()
                )
                .refreshTokenExpiresAt(
                        jwtProvider.decodeToken(request.getRefreshToken()).getExpiresAtAsInstant())
                .build();

        return ResponseEntity.ok(authResponse);
    }
}
