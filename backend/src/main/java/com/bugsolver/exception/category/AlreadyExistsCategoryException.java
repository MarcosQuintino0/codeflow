package com.bugsolver.exception.category;

import com.bugsolver.exception.BugSolverException;
import org.springframework.http.HttpStatus;

public class AlreadyExistsCategoryException extends BugSolverException {

    private static final String MESSAGE_KEY = "category.already-exists";

    public AlreadyExistsCategoryException(){
        super(HttpStatus.BAD_REQUEST,MESSAGE_KEY);
    }
}
