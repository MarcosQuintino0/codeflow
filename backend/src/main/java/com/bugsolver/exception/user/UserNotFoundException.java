package com.bugsolver.exception.user;

import com.bugsolver.exception.BugSolverException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BugSolverException {

    private static final String MESSAGE_KEY = "user.not-found";

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE_KEY);
    }
}
