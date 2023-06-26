package com.bugsolver.exception.reply;

import com.bugsolver.exception.BugSolverException;
import org.springframework.http.HttpStatus;

public class NotReplyAuthorException extends BugSolverException {

    private static final String MESSAGE_KEY = "reply.not-author";
    public NotReplyAuthorException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE_KEY);
    }
}
