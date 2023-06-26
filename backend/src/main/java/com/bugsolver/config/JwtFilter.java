package com.bugsolver.config;

import com.bugsolver.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token = getTokenFromHeader(request);

        if(token.isPresent()){
            if(jwtProvider.isValid(token.get())){
                authenticate(token.get());
            }
            else{
                response.sendError(401);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public void authenticate(String token){
        var subject = jwtProvider.getSubject(token);
        var roles = jwtProvider.getRole(token);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        subject,
                        null,
                        List.of(new SimpleGrantedAuthority(roles))
                )
        );
    }

    private Optional<String> getTokenFromHeader(HttpServletRequest request){
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(isValidToken(token)){
            return Optional.of(token.substring(7));
        }

        return Optional.empty();
    }

    public boolean isValidToken(String token){
        return nonNull(token) && token.length() > 7 && token.startsWith("Bearer ");
    }

}
