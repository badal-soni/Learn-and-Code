package com.intimetec.newsaggregation.adapter.news;

import com.intimetec.newsaggregation.classifier.NewsCategoryClassifier;
import com.intimetec.newsaggregation.entity.ExternalServerDetail;
import com.intimetec.newsaggregation.entity.News;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.repository.BlockedKeywordRepository;
import com.intimetec.newsaggregation.repository.ExternalServerDetailsRepository;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.repository.NewsRepository;
import com.intimetec.newsaggregation.util.ApiClient;
import com.intimetec.newsaggregation.util.MockDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsApiAdapterTest {

    private NewsApiAdapter newsApiAdapter;
    private NewsRepository mockNewsRepository;
    private NewsCategoryRepository mockNewsCategoryRepository;
    private NewsCategoryClassifier mockNewsCategoryClassifier;
    private ApiClient mockApiClient;
    private ExternalServerDetailsRepository mockExternalServerDetailsRepository;
    private BlockedKeywordRepository mockBlockedKeywordRepository;

    @BeforeEach
    void setUp() {
        mockNewsRepository = mock(NewsRepository.class);
        mockNewsCategoryRepository = mock(NewsCategoryRepository.class);
        mockNewsCategoryClassifier = mock(NewsCategoryClassifier.class);
        mockApiClient = mock(ApiClient.class);
        mockExternalServerDetailsRepository = mock(ExternalServerDetailsRepository.class);
        mockBlockedKeywordRepository = mock(BlockedKeywordRepository.class);

        newsApiAdapter = new NewsApiAdapter(
                mockNewsRepository,
                mockNewsCategoryRepository,
                mockExternalServerDetailsRepository,
                mockApiClient,
                mockNewsCategoryClassifier,
                mockBlockedKeywordRepository
        );
    }

    @Test
    void testFetchNewsSuccess() {
        String mockResponse = "{\"articles\": [{\"title\": \"Headline\", \"content\": \"Description\", \"url\": \"http://example.com\", \"source\": {\"name\": \"Source\"}, \"publishedAt\": \"2023-10-01T10:00:00Z\"}]}";
        List<News> mockNewsList = List.of(MockDataCreator.createMockNews());

        when(mockExternalServerDetailsRepository.findByServerName(anyString())).thenReturn(Optional.of(MockDataCreator.createMockExternalServerDetail()));
        when(mockApiClient.get(anyString(), eq(String.class))).thenReturn(mockResponse);
        when(mockExternalServerDetailsRepository.saveAndFlush(any(ExternalServerDetail.class))).thenReturn(MockDataCreator.createMockExternalServerDetail());
        when(mockNewsRepository.saveAll(anyList())).thenReturn(mockNewsList);
        when(mockNewsCategoryClassifier.classifyNewsCategory(anyString(), anyString())).thenReturn("category");
        when(mockNewsCategoryRepository.findByCategoryName(anyString())).thenReturn(Optional.of(MockDataCreator.createMockNewsCategory()));

        List<News> fetchedNews = newsApiAdapter.fetchNews();

        assertNotNull(fetchedNews);
        assertEquals(1, fetchedNews.size());
        verify(mockNewsRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testFetchNewsEmptyResponse() {
        when(mockApiClient.get(anyString(), eq(String.class))).thenReturn(null);

        List<News> fetchedNews = newsApiAdapter.fetchNews();

        assertNotNull(fetchedNews);
        assertTrue(fetchedNews.isEmpty());
        verify(mockNewsRepository, times(0)).saveAll(anyList());
    }

    @Test
    void testFetchNewsExceptionHandling() {
        String mockResponse = "{\"articles\": [{\"title\": \"Headline\", \"content\": \"Description\", \"url\": \"http://example.com\", \"source\": {\"name\": \"Source\"}, \"publishedAt\": \"2023-10-01T10:00:00Z\"}]}";

        when(mockApiClient.get(anyString(), eq(String.class))).thenReturn(mockResponse);
        when(mockExternalServerDetailsRepository.findByServerName(anyString())).thenReturn(Optional.of(MockDataCreator.createMockExternalServerDetail()));
        when(mockNewsRepository.saveAll(anyList())).thenThrow(new RuntimeException("Database error"));

        List<News> fetchedNews = newsApiAdapter.fetchNews();

        assertNotNull(fetchedNews);
        assertTrue(fetchedNews.isEmpty());
        verify(mockNewsRepository, never()).saveAll(anyList());
    }

    @Test
    void testFetchNewsWithCategoryCreation() {
        String mockResponse = "{\"articles\": [{\"title\": \"Headline\", \"content\": \"Description\", \"url\": \"http://example.com\", \"source\": {\"name\": \"Source\"}, \"publishedAt\": \"2023-10-01T10:00:00Z\"}]}";
        NewsCategory mockCategory = new NewsCategory();
        mockCategory.setCategoryName("Sports");
        mockCategory.setHidden(false);

        News mockNews = News.builder()
                .headline("Headline")
                .description("Description")
                .url("http://example.com")
                .categories(List.of(mockCategory))
                .build();

        when(mockApiClient.get(anyString(), eq(String.class))).thenReturn(mockResponse);
        when(mockExternalServerDetailsRepository.findByServerName(anyString())).thenReturn(Optional.of(MockDataCreator.createMockExternalServerDetail()));
        when(mockNewsCategoryRepository.findByCategoryName("Sports")).thenReturn(Optional.empty());
        when(mockNewsCategoryRepository.saveAndFlush(any(NewsCategory.class))).thenReturn(mockCategory);
        when(mockNewsRepository.saveAll(anyList())).thenReturn(List.of(mockNews));
        when(mockNewsCategoryClassifier.classifyNewsCategory(anyString(), anyString())).thenReturn("Sports");

        List<News> fetchedNews = newsApiAdapter.fetchNews();

        assertNotNull(fetchedNews);
        assertEquals(1, fetchedNews.size());
        assertEquals("Sports", fetchedNews.get(0).getCategories().get(0).getCategoryName());
        verify(mockNewsCategoryRepository, times(1)).saveAndFlush(any(NewsCategory.class));
        verify(mockNewsCategoryClassifier, times(1)).classifyNewsCategory(anyString(), anyString());
    }

}