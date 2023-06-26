package com.bugsolver.exception.auth;

import com.bugsolver.exception.BugSolverException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BugSolverException {

    private static final String MESSAGE_KEY = "auth.invalid-credentials";
    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, MESSAGE_KEY);
    }
}
