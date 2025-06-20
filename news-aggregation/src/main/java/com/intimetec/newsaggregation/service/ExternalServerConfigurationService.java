package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.request.RegisterExternalServerRequest;
import com.intimetec.newsaggregation.dto.request.UpdateExternalServerDetailsRequest;
import com.intimetec.newsaggregation.dto.response.ExternalServerStatusResponse;

import java.util.List;

public interface ExternalServerConfigurationService {

    void registerExternalServer(RegisterExternalServerRequest registerExternalServerRequest);

    List<ExternalServerStatusResponse> listExternalServers();

    void updateExternalServer(UpdateExternalServerDetailsRequest updateExternalServerDetailsRequest);

}
