package com.bugsolver.exception.reply;

import com.bugsolver.exception.BugSolverException;
import org.springframework.http.HttpStatus;

public class ReplyNotFoundException extends BugSolverException {

    private static final String MESSAGE_KEY = "reply.not-found";

    public ReplyNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE_KEY);
    }
}
