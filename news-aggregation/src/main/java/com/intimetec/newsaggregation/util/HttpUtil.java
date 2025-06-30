package com.intimetec.newsaggregation.util;

import com.intimetec.newsaggregation.constant.Constants;
import com.intimetec.newsaggregation.dto.response.ApiSuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class HttpUtil {

    private HttpUtil() {
    }

    public static ResponseEntity<ApiSuccessResponse> noBodyOkResponse() {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public static ResponseEntity<ApiSuccessResponse> bodyOkResponse(Object data) {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .data(data)
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public static ResponseEntity<ApiSuccessResponse> bodyCreatedResponse(Object data) {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .data(data)
                .message(HttpStatus.CREATED.getReasonPhrase())
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

}
