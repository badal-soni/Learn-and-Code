package com.itt.atm.exception;

import com.itt.atm.dto.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            AttemptsExhaustedException.class,
            CardBlockedException.class,
            DailyLimitExceededException.class,
            InSufficientBalance.class
    })
    public ResponseEntity<ApiErrorResponse> handleAttemptsExhaustedException(AttemptsExhaustedException e) {
        return buildApiErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<ApiErrorResponse> buildApiErrorResponse(HttpStatus status, String message) {
        return ApiErrorResponse
                .builder()
                .success(false)
                .message(message)
                .httpStatus(status)
                .error(message)
                .build();
    }

}
