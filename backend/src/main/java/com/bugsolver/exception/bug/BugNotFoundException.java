package com.bugsolver.exception.bug;

import com.bugsolver.exception.BugSolverException;
import org.springframework.http.HttpStatus;

public class BugNotFoundException extends BugSolverException {

    private static final String MESSAGE_KEY = "bug.not-found";

    public BugNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE_KEY);
    }
}
