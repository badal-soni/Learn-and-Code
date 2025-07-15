package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.constant.Messages;
import com.intimetec.newsaggregation.dto.request.RegisterExternalServerRequest;
import com.intimetec.newsaggregation.dto.request.UpdateExternalServerDetailsRequest;
import com.intimetec.newsaggregation.dto.response.ExternalServerStatusResponse;
import com.intimetec.newsaggregation.entity.ExternalServerDetail;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.repository.ExternalServerDetailsRepository;
import com.intimetec.newsaggregation.service.impl.ExternalServerConfigurationServiceImpl;
import com.intimetec.newsaggregation.util.MockDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExternalServerConfigurationServiceImplTest {

    @Mock
    private ExternalServerDetailsRepository externalServerDetailsRepository;

    @InjectMocks
    private ExternalServerConfigurationServiceImpl externalServerConfigurationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerExternalServer_shouldThrowExceptionIfServerExists() {
        RegisterExternalServerRequest request = MockDataCreator.createMockRegisterExternalServerRequest();
        when(externalServerDetailsRepository.existsByApiKeyOrServerName(request.getApiKey(), request.getServerName())).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> externalServerConfigurationService.registerExternalServer(request));
        assertEquals(Messages.EXTERNAL_SERVER_ALREADY_EXISTS, exception.getMessage());
        verify(externalServerDetailsRepository, times(1)).existsByApiKeyOrServerName(request.getApiKey(), request.getServerName());
    }

    @Test
    void registerExternalServer_shouldSaveServerIfNotExists() {
        RegisterExternalServerRequest request = MockDataCreator.createMockRegisterExternalServerRequest();
        when(externalServerDetailsRepository.existsByApiKeyOrServerName(request.getApiKey(), request.getServerName())).thenReturn(false);

        externalServerConfigurationService.registerExternalServer(request);

        verify(externalServerDetailsRepository, times(1)).save(any(ExternalServerDetail.class));
    }

    @Test
    void listExternalServers_shouldReturnMappedResponses() {
        ExternalServerDetail serverDetail = MockDataCreator.createMockExternalServerDetail();
        when(externalServerDetailsRepository.findAll()).thenReturn(List.of(serverDetail));

        List<ExternalServerStatusResponse> responses = externalServerConfigurationService.listExternalServers();

        assertEquals(1, responses.size());
        assertEquals("serverName", responses.get(0).getServerName());
        verify(externalServerDetailsRepository, times(1)).findAll();
    }

    @Test
    void updateExternalServer_shouldThrowExceptionIfServerNotFound() {
        UpdateExternalServerDetailsRequest request = MockDataCreator.createMockUpdateExternalServerDetailsRequest();
        when(externalServerDetailsRepository.findById(request.getServerId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> externalServerConfigurationService.updateExternalServer(request));
        assertEquals(String.format(Messages.EXTERNAL_SERVER_NOT_FOUND_BY_ID, request.getServerId()), exception.getMessage());
        verify(externalServerDetailsRepository, times(1)).findById(request.getServerId());
    }

    @Test
    void updateExternalServer_shouldUpdateServerIfFound() {
        UpdateExternalServerDetailsRequest request = MockDataCreator.createMockUpdateExternalServerDetailsRequest();
        ExternalServerDetail serverDetail = new ExternalServerDetail();
        when(externalServerDetailsRepository.findById(request.getServerId())).thenReturn(Optional.of(serverDetail));

        externalServerConfigurationService.updateExternalServer(request);

        assertEquals("apiKey", serverDetail.getApiKey());
        assertTrue(serverDetail.isActive());
        verify(externalServerDetailsRepository, times(1)).save(serverDetail);
    }

}