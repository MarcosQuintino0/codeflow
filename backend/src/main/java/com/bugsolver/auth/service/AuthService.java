package com.bugsolver.auth.service;

import com.bugsolver.auth.entity.AuthTokenRequest;
import com.bugsolver.auth.entity.AuthRole;
import com.bugsolver.entity.User;
import com.bugsolver.exception.auth.InvalidCredentialsException;
import com.bugsolver.exception.user.UserNotFoundException;
import com.bugsolver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public void authenticate(AuthTokenRequest request){
        try{
            User user = userService.findByUsername(request.getUsername());

            if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
                var authentication = authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword(),
                                List.of(new SimpleGrantedAuthority(AuthRole.AUTH.name()))
                        )
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            else{
                throw new InvalidCredentialsException();
            }

        } catch(AuthenticationException e){
            throw new InvalidCredentialsException();
        } catch(UserNotFoundException e){
            throw new UserNotFoundException();
        }
    }
}
