package com.bugsolver.exception.user;

import com.bugsolver.exception.BugSolverException;
import org.springframework.http.HttpStatus;

public class AlreadyUsedEmailException extends BugSolverException {

    private static final String MESSAGE_KEY = "user.email.already-exists";
    public AlreadyUsedEmailException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE_KEY);
    }
}
