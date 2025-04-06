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
public final class ApiSuccessResponse {

    @JsonProperty(value = "success")
    private boolean isSuccess;
    private Object data;
    private String message;

    private ApiSuccessResponse() {
    }

    public static ApiSuccessResponseBuilder builder() {
        return new ApiSuccessResponseBuilder();
    }

    public static final class ApiSuccessResponseBuilder {

        private Object data;
        private String message;
        private HttpStatus httpStatus;

        public ApiSuccessResponseBuilder data(Object data) {
            this.data = data;
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

        public ResponseEntity<ApiSuccessResponse> build() {
            ApiSuccessResponse response = new ApiSuccessResponse();
            response.data = data;
            response.message = message;
            response.isSuccess = Constants.SUCCESS_TRUE;
            return new ResponseEntity<>(response, this.httpStatus);
        }

    }

}
