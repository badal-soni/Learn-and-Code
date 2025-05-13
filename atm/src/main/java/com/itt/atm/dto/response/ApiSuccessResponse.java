package com.itt.atm.dto.response;

import com.itt.atm.dto.ApiResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class ApiSuccessResponse extends ApiResponse {

    private Object data;

    private ApiSuccessResponse() {

    }

    public static ApiSuccessResponseBuilder builder() {
        return new ApiSuccessResponseBuilder();
    }

    public static class ApiSuccessResponseBuilder extends ApiResponse {
        private Object data;

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
            ApiSuccessResponse apiSuccessResponse = new ApiSuccessResponse();
            apiSuccessResponse.isSuccess = this.isSuccess;
            apiSuccessResponse.message = this.message;
            apiSuccessResponse.httpStatus = this.httpStatus;
            apiSuccessResponse.data = this.data;
            return new ResponseEntity<>(apiSuccessResponse, this.httpStatus);
        }
    }

}
