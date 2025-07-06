package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.Keywords;
import com.intimetec.newsaggregation.dto.NewsIds;
import com.intimetec.newsaggregation.dto.event.NewsReportedEvent;
import com.intimetec.newsaggregation.dto.request.ReportNewsArticleRequest;
import com.intimetec.newsaggregation.dto.response.ReportedNewsResponse;
import com.intimetec.newsaggregation.entity.News;
import com.intimetec.newsaggregation.entity.ReportedNews;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.repository.NewsRepository;
import com.intimetec.newsaggregation.repository.ReportedNewsRepository;
import com.intimetec.newsaggregation.service.impl.ReportNewsServiceImpl;
import com.intimetec.newsaggregation.util.MockDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReportNewsServiceImplTest {

    @Mock
    private ReportedNewsRepository reportedNewsRepository;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ReportNewsServiceImpl reportNewsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void reportNews_shouldReportNews() {
        User user = MockDataCreator.createMockUser();
        News news = MockDataCreator.createMockNews();
        ReportNewsArticleRequest request = MockDataCreator.createMockReportNewsArticleRequest();
        ReportedNews reportedNews = MockDataCreator.createMockReportedNews();

        when(reportedNewsRepository.existsByReportedNewsIdAndReportedByAndReportedNewsIsHiddenFalse(news.getId(), user)).thenReturn(false);
        when(newsRepository.findById(news.getId())).thenReturn(Optional.of(news));
        when(reportedNewsRepository.saveAndFlush(any())).thenReturn(reportedNews);

        reportNewsService.reportNews(news.getId(), request, user);

        verify(reportedNewsRepository, times(1)).saveAndFlush(any());
        verify(eventPublisher, times(1)).publishEvent(any(NewsReportedEvent.class));
    }

    @Test
    void hideNews_shouldHideNews() {
        NewsIds newsIds = MockDataCreator.createMockNewsIds();
        reportNewsService.hideNews(newsIds);
        verify(newsRepository, times(1)).updateHiddenStatusByIds(true, newsIds.getNewsIds());
    }

    @Test
    void unHideNews_shouldUnHideNews() {
        NewsIds newsIds = MockDataCreator.createMockNewsIds();
        reportNewsService.unHideNews(newsIds);
        verify(newsRepository, times(1)).updateHiddenStatusByIds(false, newsIds.getNewsIds());
    }

    @Test
    void hideByKeywords_shouldHideByKeywords() {
        Keywords keywords = MockDataCreator.createMockKeywords();
        reportNewsService.hideByKeywords(keywords);
        verify(newsRepository, times(1)).setIsHiddenTrue(anyString());
    }

    @Test
    void getAllReportedNews_shouldReturnReportedNews() {
        ReportedNews reportedNews = MockDataCreator.createMockReportedNews();
        reportedNews.setCreatedAt(LocalDateTime.now());
        when(reportedNewsRepository.findAll()).thenReturn(List.of(reportedNews));
        List<ReportedNewsResponse> responses = reportNewsService.getAllReportedNews();
        assertEquals(1, responses.size());
    }
}