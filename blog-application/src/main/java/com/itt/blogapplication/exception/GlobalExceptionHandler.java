package com.itt.blogapplication.exception;

import com.itt.blogapplication.dto.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException exception) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus
    ) {
        return ApiErrorResponse
                .builder()
                .httpStatus(httpStatus)
                .error(exception.getMessage())
                .build();
    }

}
