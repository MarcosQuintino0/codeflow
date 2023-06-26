package com.bugsolver.exception.user;

import com.bugsolver.exception.BugSolverException;
import org.springframework.http.HttpStatus;

public class NotBugAuthorException extends BugSolverException {

    public static final String MESSAGE_KEY = "user.not-bug-author";

    public NotBugAuthorException(){
        super(HttpStatus.FORBIDDEN, MESSAGE_KEY);
    }

}
