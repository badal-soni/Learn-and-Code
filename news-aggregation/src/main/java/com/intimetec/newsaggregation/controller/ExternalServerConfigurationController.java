package com.intimetec.newsaggregation.controller;

import com.intimetec.newsaggregation.constant.ApiVersions;
import com.intimetec.newsaggregation.constant.Constants;
import com.intimetec.newsaggregation.dto.request.RegisterExternalServerRequest;
import com.intimetec.newsaggregation.dto.request.UpdateExternalServerDetailsRequest;
import com.intimetec.newsaggregation.dto.response.ApiSuccessResponse;
import com.intimetec.newsaggregation.service.ExternalServerConfigurationService;
import com.intimetec.newsaggregation.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        path = ApiVersions.V1_SERVER_CONFIG,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Secured(value = Constants.SPRING_ROLE_ADMIN)
@RequiredArgsConstructor
public class ExternalServerConfigurationController {

    private final ExternalServerConfigurationService externalServerConfigurationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse> registerExternalServer(@RequestBody RegisterExternalServerRequest registerExternalServerRequest) {
        externalServerConfigurationService.registerExternalServer(registerExternalServerRequest);
        return HttpUtil.sendResponseWithNoData(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiSuccessResponse> getAllServerConfigurations() {
        return HttpUtil.sendResponseWithData(
                externalServerConfigurationService.listExternalServers(),
                HttpStatus.OK
        );
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse> updateExternalServerConfiguration(@RequestBody UpdateExternalServerDetailsRequest updateExternalServerDetailsRequest) {
        externalServerConfigurationService.updateExternalServer(updateExternalServerDetailsRequest);
        return HttpUtil.sendResponseWithNoData(HttpStatus.OK);
    }

}
