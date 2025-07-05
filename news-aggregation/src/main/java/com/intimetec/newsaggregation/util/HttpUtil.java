package com.intimetec.newsaggregation.util;

import com.intimetec.newsaggregation.constant.Constants;
import com.intimetec.newsaggregation.dto.response.ApiSuccessResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class HttpUtil {

    private HttpUtil() {
    }

    public static ResponseEntity<ApiSuccessResponse> sendResponseWithNoData(@NotNull HttpStatus httpStatus) {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .message(httpStatus.getReasonPhrase())
                .httpStatus(httpStatus)
                .build();
    }

    public static ResponseEntity<ApiSuccessResponse> sendResponseWithData(
            @NotNull Object data,
            @NotNull HttpStatus httpStatus
    ) {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .data(data)
                .message(httpStatus.getReasonPhrase())
                .httpStatus(httpStatus)
                .build();
    }

}
