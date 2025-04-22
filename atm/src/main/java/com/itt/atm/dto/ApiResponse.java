package com.itt.atm.dto;

import org.springframework.http.HttpStatus;

public abstract class ApiResponse {

    protected boolean isSuccess;
    protected String message;
    protected HttpStatus httpStatus;

}
