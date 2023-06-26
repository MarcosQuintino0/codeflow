package com.bugsolver.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bugsolver.auth.entity.AuthRole;
import com.bugsolver.exception.auth.InvalidRefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.bearer-token.expires-at}")
    private Integer bearerTokenExpiresAt;

    @Value("${jwt.refresh-token.expires-at}")
    private Integer refreshTokenExpiresAt;

    private final String ROLE_KEY = "ROLE";

    public String createAuthToken(String subject){
        return createToken(subject, AuthRole.AUTH.toString(), bearerTokenExpiresAt);
    }

    public String createRefreshToken(String subject){
        return createToken(subject, AuthRole.REFRESH.toString(), refreshTokenExpiresAt);
    }

    public String refreshAuthToken(String refreshToken){
        if(isValid(refreshToken) && getRole(refreshToken).equals(AuthRole.REFRESH.toString())){
            return createAuthToken(getSubject(refreshToken));
        } else{
            throw new InvalidRefreshToken();
        }
    }

    public String createToken(String subject, String role, Integer expirationTime){
        return JWT.create()
                .withSubject(subject)
                .withClaim("ROLE", role)
                .withExpiresAt(Instant.now().plus(expirationTime, ChronoUnit.MINUTES))
                .sign(getAlgorithm());
    }

    public boolean isValid(String token){
        try{
            decodeToken(token);
            return true;
        } catch(JWTVerificationException e){
            return false;
        }
    }

    public DecodedJWT decodeToken(String token){
        var tokenVerifier = JWT.require(getAlgorithm()).build();
        return tokenVerifier.verify(token);
    }

    public String getSubject(String token){
        var decodedToken = decodeToken(token);
        return decodedToken.getSubject();
    }

    public String getRole(String token){
        var decodedToken = decodeToken(token);
        return decodedToken.getClaim("ROLE").asString();
    }

    public Algorithm getAlgorithm(){
        var secretEncoded = Base64.getEncoder().encode(jwtSecret.getBytes());
        return Algorithm.HMAC512(secretEncoded);
    }

}
