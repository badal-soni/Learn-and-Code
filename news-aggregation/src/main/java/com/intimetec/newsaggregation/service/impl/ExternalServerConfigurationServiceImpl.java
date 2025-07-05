package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.constant.Messages;
import com.intimetec.newsaggregation.dto.request.RegisterExternalServerRequest;
import com.intimetec.newsaggregation.dto.request.UpdateExternalServerDetailsRequest;
import com.intimetec.newsaggregation.dto.response.ExternalServerStatusResponse;
import com.intimetec.newsaggregation.entity.ExternalServerDetail;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.repository.ExternalServerDetailsRepository;
import com.intimetec.newsaggregation.service.ExternalServerConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalServerConfigurationServiceImpl implements ExternalServerConfigurationService {

    private final ExternalServerDetailsRepository externalServerDetailsRepository;

    @Override
    public void registerExternalServer(RegisterExternalServerRequest registerExternalServerRequest) {
        if (externalServerDetailsRepository.existsByApiKeyOrServerName(registerExternalServerRequest.getApiKey(), registerExternalServerRequest.getServerName())) {
            log.warn(Messages.EXTERNAL_SERVER_ALREADY_EXISTS);
            throw new BadRequestException(Messages.EXTERNAL_SERVER_ALREADY_EXISTS);
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
                    response.setServerId(server.getId());
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
                .orElseThrow(() -> new IllegalArgumentException(String.format(Messages.EXTERNAL_SERVER_NOT_FOUND_BY_ID, updateExternalServerDetailsRequest.getServerId())));

        externalServerDetail.setApiKey(updateExternalServerDetailsRequest.getApiKeyToUpdate());
        externalServerDetail.setActive(updateExternalServerDetailsRequest.isEnabled());
        externalServerDetailsRepository.save(externalServerDetail);
    }

}
