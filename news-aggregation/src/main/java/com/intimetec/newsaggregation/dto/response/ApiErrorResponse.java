package com.intimetec.newsaggregation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@Data
@AllArgsConstructor
public class ApiErrorResponse {

    private Object error;
    private boolean isSuccess;
    private String message;
    private HttpStatus httpStatus;

    private ApiErrorResponse() {
    }

    public static ApiErrorResponseBuilder builder() {

        return new ApiErrorResponseBuilder();
    }

    public static final class ApiErrorResponseBuilder {

        private Object error;
        private boolean isSuccess;
        private String message;
        private HttpStatus httpStatus;

        public ApiErrorResponseBuilder success(boolean success) {
            this.isSuccess = success;
            return this;
        }

        public ApiErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ApiErrorResponseBuilder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ApiErrorResponseBuilder error(Object error) {
            this.error = error;
            return this;
        }

        public ResponseEntity<ApiErrorResponse> build() {
            this.httpStatus = Objects.isNull(httpStatus) ? HttpStatus.OK : httpStatus;
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
            apiErrorResponse.setSuccess(this.isSuccess);
            apiErrorResponse.setMessage(this.message);
            apiErrorResponse.setHttpStatus(this.httpStatus);
            apiErrorResponse.setError(this.error);

            return ResponseEntity
                    .status(this.httpStatus)
                    .body(apiErrorResponse);
        }

    }
}
