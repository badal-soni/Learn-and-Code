package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.dto.request.RegisterExternalServerRequest;
import com.intimetec.newsaggregation.dto.request.UpdateExternalServerDetailsRequest;
import com.intimetec.newsaggregation.dto.response.ExternalServerStatusResponse;
import com.intimetec.newsaggregation.entity.ExternalServerDetail;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.repository.ExternalServerDetailsRepository;
import com.intimetec.newsaggregation.service.ExternalServerConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalServerConfigurationServiceImpl implements ExternalServerConfigurationService {

    private final ExternalServerDetailsRepository externalServerDetailsRepository;

    @Override
    public void registerExternalServer(RegisterExternalServerRequest registerExternalServerRequest) {
        if (externalServerDetailsRepository.existsByApiKeyOrServerName(registerExternalServerRequest.getApiKey(), registerExternalServerRequest.getServerName())) {
            throw new BadRequestException("An external server is already registered with same name or api key");
        }

        ExternalServerDetail externalServerDetail = new ExternalServerDetail();
        externalServerDetail.setServerName(registerExternalServerRequest.getServerName());
        externalServerDetail.setApiKey(registerExternalServerRequest.getApiKey());
        externalServerDetail.setRequestUri(registerExternalServerRequest.getApiUrl());
        externalServerDetail.setActive(true);
        externalServerDetailsRepository.save(externalServerDetail);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExternalServerStatusResponse> listExternalServers() {
        return externalServerDetailsRepository
                .findAll()
                .stream()
                .map(server -> {
                    final ExternalServerStatusResponse response = new ExternalServerStatusResponse();
                    response.setServerName(server.getServerName());
                    response.setLastAccessedDate(server.getLastAccessedDate());
                    response.setApiKey(server.getApiKey());
                    response.setActiveStatus(server.isActive());
                    response.setLastFailedTime(server.getLastFailedTime());
                    return response;
                })
                .toList();
    }

    @Override
    public void updateExternalServer(UpdateExternalServerDetailsRequest updateExternalServerDetailsRequest) {
        ExternalServerDetail externalServerDetail = externalServerDetailsRepository
                .findById(updateExternalServerDetailsRequest.getServerId())
                .orElseThrow(() -> new IllegalArgumentException("External server not found"));

        externalServerDetail.setApiKey(updateExternalServerDetailsRequest.getApiKeyToUpdate());
        externalServerDetail.setActive(updateExternalServerDetailsRequest.isEnabled());
        externalServerDetailsRepository.save(externalServerDetail);
    }

}
