package com.intimetec.newsaggregation.worker;

import com.intimetec.newsaggregation.adapter.news.NewsFetcher;
import com.intimetec.newsaggregation.dto.event.NewsFetchedEvent;
import com.intimetec.newsaggregation.entity.News;
import com.intimetec.newsaggregation.util.MockDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class NewsSchedulerTest {

    private NewsScheduler newsScheduler;
    private List<NewsFetcher> mockNewsFetchers;
    private ApplicationEventPublisher mockEventPublisher;

    @BeforeEach
    void setUp() {
        mockNewsFetchers = List.of(mock(NewsFetcher.class), mock(NewsFetcher.class));
        mockEventPublisher = mock(ApplicationEventPublisher.class);
        newsScheduler = new NewsScheduler(mockNewsFetchers, mockEventPublisher);
    }

    @Test
    void testFetchNewsFromExternalDataSourceWithNonHiddenNews() {
        NewsFetcher mockFetcher = mockNewsFetchers.get(0);
        List<News> mockNews = List.of(
                MockDataCreator.createMockNews(),
                MockDataCreator.createMockNews()
        );

        when(mockFetcher.fetchNews()).thenReturn(mockNews);

        newsScheduler.fetchNewsFromExternalDataSource();

        ArgumentCaptor<NewsFetchedEvent> eventCaptor = ArgumentCaptor.forClass(NewsFetchedEvent.class);
        verify(mockEventPublisher, times(1)).publishEvent(eventCaptor.capture());

        NewsFetchedEvent capturedEvent = eventCaptor.getValue();
        assertNotNull(capturedEvent);
        assertEquals(mockNews, capturedEvent.getFetchedNews());
    }

    @Test
    void testFetchNewsFromExternalDataSourceWithHiddenNews() {
        NewsFetcher mockFetcher = mockNewsFetchers.get(0);
        List<News> mockNews = List.of(
                MockDataCreator.createMockNews(),
                MockDataCreator.createMockNews()
        );

        when(mockFetcher.fetchNews()).thenReturn(mockNews);

        newsScheduler.fetchNewsFromExternalDataSource();

        verify(mockEventPublisher, times(0)).publishEvent(any());
    }

    @Test
    void testFetchNewsFromExternalDataSourceWithEmptyNews() {
        NewsFetcher mockFetcher = mockNewsFetchers.get(0);

        when(mockFetcher.fetchNews()).thenReturn(List.of());

        newsScheduler.fetchNewsFromExternalDataSource();

        verify(mockEventPublisher, times(0)).publishEvent(any());
    }

    @Test
    void testFetchNewsFromExternalDataSourceWithMultipleFetchers() {
        NewsFetcher mockFetcher1 = mockNewsFetchers.get(0);
        NewsFetcher mockFetcher2 = mockNewsFetchers.get(1);
        List<News> mockNews = List.of(
                MockDataCreator.createMockNews(),
                MockDataCreator.createMockNews()
        );

        when(mockFetcher1.fetchNews()).thenReturn(List.of());
        when(mockFetcher2.fetchNews()).thenReturn(mockNews);

        newsScheduler.fetchNewsFromExternalDataSource();

        ArgumentCaptor<NewsFetchedEvent> eventCaptor = ArgumentCaptor.forClass(NewsFetchedEvent.class);
        verify(mockEventPublisher, times(1)).publishEvent(eventCaptor.capture());

        NewsFetchedEvent capturedEvent = eventCaptor.getValue();
        assertNotNull(capturedEvent);
        assertEquals(mockNews, capturedEvent.getFetchedNews());
    }
}
