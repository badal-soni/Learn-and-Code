package com.itt.atm.controller;

import com.itt.atm.constant.ApiUrls;
import com.itt.atm.dto.request.WithDrawMoneyRequest;
import com.itt.atm.dto.response.ApiSuccessResponse;
import com.itt.atm.service.AtmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = ApiUrls.V1_ATM)
@RequiredArgsConstructor
public class AtmController {

    private final AtmService atmService;

    @PutMapping(
            path = "/withdraw",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiSuccessResponse> withDrawMoney(@RequestBody WithDrawMoneyRequest withDrawMoneyRequest) {
        return ApiSuccessResponse
                .builder()
                .success(true)
                .httpStatus(HttpStatus.OK)
                .message(HttpStatus.OK.getReasonPhrase())
                .data(atmService.withDrawAmount(withDrawMoneyRequest))
                .build();
    }

}