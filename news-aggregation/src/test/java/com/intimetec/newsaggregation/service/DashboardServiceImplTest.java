package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.response.SavedNewsResponse;
import com.intimetec.newsaggregation.entity.SavedArticle;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.mapper.NewsMapper;
import com.intimetec.newsaggregation.repository.SavedArticleRepository;
import com.intimetec.newsaggregation.service.impl.DashboardServiceImpl;
import com.intimetec.newsaggregation.util.MockDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DashboardServiceImplTest {

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Mock
    private SavedArticleRepository savedArticleRepository;

    @Mock
    private NewsMapper newsMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllNewsSavedByUser_success() {
        User user = User.builder()
                .email("john@example.com")
                .username("John")
                .build();
        user.setId(1L);

        List<SavedArticle> savedNews = List.of(
                MockDataCreator.createMockSavedArticle(),
                MockDataCreator.createMockSavedArticle()
        );

        List<SavedNewsResponse> expectedResponse = List.of(
                MockDataCreator.createMockSavedNewsResponse(),
                MockDataCreator.createMockSavedNewsResponse()
        );

        when(savedArticleRepository.findAllBySavedById(user.getId())).thenReturn(savedNews);

        try (MockedStatic<NewsMapper> mockedNewsMapper = mockStatic(NewsMapper.class)) {
            mockedNewsMapper.when(() -> NewsMapper.mapToSavedNewsResponse(savedNews)).thenReturn(expectedResponse);

            List<SavedNewsResponse> actualResponse = dashboardService.getAllNewsSavedByUser(user);

            assertEquals(expectedResponse, actualResponse);
            verify(savedArticleRepository, times(1)).findAllBySavedById(user.getId());
        }
    }
}