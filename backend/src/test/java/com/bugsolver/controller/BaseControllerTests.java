package com.bugsolver.controller;

import com.bugsolver.auth.entity.AuthRole;
import com.bugsolver.auth.service.CustomUserDetailsService;
import com.bugsolver.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseControllerTests {

    protected static final String LOGGED_USERNAME = "mike";

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${app.language-default}")
    private String appLanguageDefault;

    protected String getMessage(String messageKey) {
        return messageSource.getMessage(messageKey, null, new Locale(appLanguageDefault));
    }

    protected String getAuthToken() {
        var user = new User(LOGGED_USERNAME, "", List.of(new SimpleGrantedAuthority(AuthRole.AUTH.toString())));
        when(userDetailsService.loadUserByUsername(LOGGED_USERNAME)).thenReturn(user);
        return "Bearer " + jwtProvider.createAuthToken(LOGGED_USERNAME);
    }
}
