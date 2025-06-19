package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.dto.request.RegisterExternalServerRequest;
import com.intimetec.newsaggregation.dto.request.UpdateExternalServerDetailsRequest;
import com.intimetec.newsaggregation.dto.response.ExternalServerStatusResponse;
import com.intimetec.newsaggregation.entity.ExternalServerDetail;
import com.intimetec.newsaggregation.repository.ExternalServerDetailsRepository;
import com.intimetec.newsaggregation.service.ExternalServerConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalServerConfigurationServiceImpl implements ExternalServerConfigurationService {

    private final ExternalServerDetailsRepository externalServerDetailsRepository;

    @Override
    public void registerExternalServer(RegisterExternalServerRequest registerExternalServerRequest) {
        ExternalServerDetail externalServerDetail = new ExternalServerDetail();
        externalServerDetail.setServerName(registerExternalServerRequest.getServerName());
        externalServerDetail.setApiKey(registerExternalServerRequest.getApiKey());
        externalServerDetail.setRequestUri(registerExternalServerRequest.getApiUrl());
        externalServerDetail.setActive(true);
        externalServerDetailsRepository.save(externalServerDetail);
    }

    @Override
    public List<ExternalServerStatusResponse> listExternalServers() {
        return externalServerDetailsRepository.findAll()
                .stream()
                .map(server -> {
                    ExternalServerStatusResponse response = new ExternalServerStatusResponse();
                    response.setServerName(server.getServerName());
                    final LocalDate lastAccessedDate = server.getLastAccessedDate();
                    String lastAccessed = new StringBuilder()
                            .append(lastAccessedDate.getDayOfMonth()).append(' ')
                            .append(lastAccessedDate.getMonth()).append(' ')
                            .append(lastAccessedDate.getYear())
                            .toString();
                    response.setLastAccessedDate(lastAccessed);
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
        externalServerDetailsRepository.save(externalServerDetail);
    }

}
