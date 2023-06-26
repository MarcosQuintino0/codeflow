package com.bugsolver.exception.category;

import com.bugsolver.exception.BugSolverException;
import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends BugSolverException {

    public static final String MESSAGE_KEY = "category.not-found";

    public CategoryNotFoundException() {
        super(HttpStatus.NOT_FOUND,MESSAGE_KEY);
    }
}
