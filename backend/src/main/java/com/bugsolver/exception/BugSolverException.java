package com.bugsolver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BugSolverException extends ResponseStatusException {

    @Getter
    private final String messageKey;

    public BugSolverException(HttpStatus status, String messageKey){
        super(status,messageKey);
        this.messageKey = messageKey;
    }
}
