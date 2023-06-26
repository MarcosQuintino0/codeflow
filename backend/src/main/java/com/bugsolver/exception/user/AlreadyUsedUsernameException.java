package com.bugsolver.exception.user;

import com.bugsolver.exception.BugSolverException;
import org.springframework.http.HttpStatus;

public class AlreadyUsedUsernameException extends BugSolverException {

    private static final String MESSAGE_KEY = "user.username.already-exists";
    public AlreadyUsedUsernameException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE_KEY);
    }
}
