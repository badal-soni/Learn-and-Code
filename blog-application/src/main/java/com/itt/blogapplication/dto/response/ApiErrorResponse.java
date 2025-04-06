package com.itt.blogapplication.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itt.blogapplication.constant.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@AllArgsConstructor
public final class ApiErrorResponse {

    @JsonProperty(value = "success")
    private boolean isSuccess;
    private Object error;

    private ApiErrorResponse() {
    }

    public static ApiErrorResponseBuilder builder() {
        return new ApiErrorResponseBuilder();
    }

    public static final class ApiErrorResponseBuilder {

        private Object error;
        private HttpStatus httpStatus;

        public ApiErrorResponseBuilder error(Object error) {
            this.error = error;
            return this;
        }

        public ApiErrorResponseBuilder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ResponseEntity<ApiErrorResponse> build() {
            ApiErrorResponse response = new ApiErrorResponse();
            response.error = error;
            response.isSuccess = Constants.SUCCESS_FALSE;
            return new ResponseEntity<>(response, this.httpStatus);
        }

    }

}

