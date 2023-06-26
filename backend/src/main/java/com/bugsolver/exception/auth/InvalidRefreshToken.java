package com.bugsolver.exception.auth;

import com.bugsolver.exception.BugSolverException;
import org.springframework.http.HttpStatus;

public class InvalidRefreshToken extends BugSolverException {

    private static final String MESSAGE_KEY = "auth.refresh-token-invalid";

    public InvalidRefreshToken() {
        super(HttpStatus.FORBIDDEN, MESSAGE_KEY);
    }
}
