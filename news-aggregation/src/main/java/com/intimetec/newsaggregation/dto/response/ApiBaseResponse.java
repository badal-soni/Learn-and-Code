package com.intimetec.newsaggregation.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public abstract class ApiBaseResponse {

    protected boolean isSuccess;
    protected String message;
    protected HttpStatus httpStatus;

}
