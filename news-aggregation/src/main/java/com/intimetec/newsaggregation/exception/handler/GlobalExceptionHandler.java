package com.intimetec.newsaggregation.exception.handler;

import com.intimetec.newsaggregation.constant.Constants;
import com.intimetec.newsaggregation.dto.response.ApiErrorResponse;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.exception.NotFoundException;
import com.intimetec.newsaggregation.exception.TokenException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException exception) {
        return buildApiErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ApiErrorResponse> handleTokenException(TokenException exception) {
        return buildApiErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestExceptionException(BadRequestException exception) {
        return buildApiErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException exception) {
        return buildApiErrorResponse(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(Throwable exception) {
        return buildApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ResponseEntity<ApiErrorResponse> buildApiErrorResponse(
            @NotNull HttpStatus status,
            @NotNull String message
    ) {
        return ApiErrorResponse.builder()
                .success(Constants.SUCCESS_FALSE)
                .message(message)
                .httpStatus(status)
                .error(message)
                .build();
    }

}
