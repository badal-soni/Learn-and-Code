package com.intimetec.newsaggregation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
public final class ApiSuccessResponse {

    private Object data;
    private boolean isSuccess;
    private String message;
    private HttpStatus httpStatus;

    private ApiSuccessResponse() {
    }

    public static ApiSuccessResponseBuilder builder() {
        return new ApiSuccessResponseBuilder();
    }

    public static class ApiSuccessResponseBuilder {

        private Object data;
        private boolean isSuccess = true;
        private String message = "Success";
        private HttpStatus httpStatus = HttpStatus.OK;

        public ApiSuccessResponseBuilder success(boolean success) {
            this.isSuccess = success;
            return this;
        }

        public ApiSuccessResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ApiSuccessResponseBuilder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ApiSuccessResponseBuilder data(Object data) {
            this.data = data;
            return this;
        }

        public ResponseEntity<ApiSuccessResponse> build() {
            ApiSuccessResponse response = new ApiSuccessResponse();
            response.setData(this.data);
            response.setSuccess(this.isSuccess);
            response.setMessage(this.message);
            response.setHttpStatus(this.httpStatus);

            return ResponseEntity.status(this.httpStatus).body(response);
        }
    }

}
