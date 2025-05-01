package com.itt.blogapplication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public abstract class ApiResponse {

    @JsonProperty(value = "success")
    protected boolean isSuccess;
    protected String message;
    protected HttpStatus httpStatus;

}
