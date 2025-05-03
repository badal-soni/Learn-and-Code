package com.itt.assignment6.exception;

public class ApiError extends RuntimeException {

    public ApiError(String message) {
        super(message);
    }

}
