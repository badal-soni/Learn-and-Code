package com.itt.atm.dto.response;

import com.itt.atm.dto.ApiResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class ApiErrorResponse extends ApiResponse {

    private Object error;

    private ApiErrorResponse() {

    }

    public static ApiErrorResponseBuilder builder() {
        return new ApiErrorResponseBuilder();
    }

    public static class ApiErrorResponseBuilder extends ApiResponse {

        private Object error;

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
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
            apiErrorResponse.isSuccess = this.isSuccess;
            apiErrorResponse.message = this.message;
            apiErrorResponse.httpStatus = this.httpStatus;
            apiErrorResponse.error = this.error;
            return new ResponseEntity<>(apiErrorResponse, this.httpStatus);
        }

    }

}
