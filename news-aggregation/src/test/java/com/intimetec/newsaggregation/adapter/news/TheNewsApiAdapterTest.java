package com.intimetec.newsaggregation.adapter.news;

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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class TheNewsApiAdapterTest {


    private TheNewsApiAdapter theNewsApiAdapter;
    private NewsRepository mockNewsRepository;
    private NewsCategoryRepository mockNewsCategoryRepository;
    private ExternalServerDetailsRepository mockExternalServerDetailsRepository;
    private BlockedKeywordRepository mockBlockedKeywordRepository;
    private ApiClient mockApiClient;

    @BeforeEach
    void setUp() {
        mockNewsRepository = mock(NewsRepository.class);
        mockNewsCategoryRepository = mock(NewsCategoryRepository.class);
        mockExternalServerDetailsRepository = mock(ExternalServerDetailsRepository.class);
        mockBlockedKeywordRepository = mock(BlockedKeywordRepository.class);
        mockApiClient = mock(ApiClient.class);

        theNewsApiAdapter = new TheNewsApiAdapter(
                mockNewsRepository,
                mockNewsCategoryRepository,
                mockExternalServerDetailsRepository,
                mockBlockedKeywordRepository,
                mockApiClient
        );
    }

    @Test
    void testFetchNewsSuccess() {
        String mockResponse = "{\"data\": {\"general\": [{\"title\": \"Headline\", \"description\": \"Description\", \"url\": \"http://example.com\", \"source\": \"Source\", \"publishedAt\": \"2023-10-01T10:00:00Z\", \"categories\": [\"Sports\"]}]}}";
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
        when(mockNewsCategoryRepository.findAllByCategoryNameIn(anySet())).thenReturn(Set.of(mockCategory));
        when(mockNewsRepository.saveAll(anyList())).thenReturn(List.of(mockNews));

        List<News> fetchedNews = theNewsApiAdapter.fetchNews();
        assertNotNull(fetchedNews);
    }

    @Test
    void testFetchNewsEmptyResponse() {
        when(mockApiClient.get(anyString(), eq(String.class))).thenReturn(null);

        List<News> fetchedNews = theNewsApiAdapter.fetchNews();

        assertNotNull(fetchedNews);
        assertTrue(fetchedNews.isEmpty());
        verify(mockNewsRepository, times(0)).saveAll(anyList());
    }

    @Test
    void testFetchNewsExceptionHandling() {
        String mockResponse = "{\"data\": {\"general\": [{\"title\": \"Headline\", \"description\": \"Description\", \"url\": \"http://example.com\", \"source\": \"Source\", \"publishedAt\": \"2023-10-01T10:00:00Z\", \"categories\": [\"Sports\"]}]}}";
        List<News> mockNewsList = List.of(MockDataCreator.createMockNews());

        when(mockNewsCategoryRepository.findAllByCategoryNameIn(anySet())).thenReturn(new HashSet<>(List.of(MockDataCreator.createMockNewsCategory())));
        when(mockNewsCategoryRepository.saveAllAndFlush(anyCollection())).thenReturn(List.of(MockDataCreator.createMockNewsCategory()));
        when(mockApiClient.get(anyString(), eq(String.class))).thenReturn(mockResponse);
        when(mockExternalServerDetailsRepository.findByServerName(anyString())).thenReturn(Optional.of(MockDataCreator.createMockExternalServerDetail()));
        when(mockNewsRepository.saveAll(anyList())).thenReturn(mockNewsList);
        when(mockExternalServerDetailsRepository.findByServerName(anyString())).thenReturn(Optional.of(MockDataCreator.createMockExternalServerDetail()));
        when(mockApiClient.get(anyString(), eq(String.class))).thenReturn(mockResponse);
        when(mockExternalServerDetailsRepository.saveAndFlush(any(ExternalServerDetail.class))).thenReturn(MockDataCreator.createMockExternalServerDetail());
        when(mockNewsRepository.saveAll(anyList())).thenReturn(mockNewsList);
        when(mockBlockedKeywordRepository.findAll()).thenReturn(List.of(MockDataCreator.createMockBlockedKeyword()));
        when(mockNewsCategoryRepository.findByCategoryName(anyString())).thenReturn(Optional.of(MockDataCreator.createMockNewsCategory()));

        List<News> fetchedNews = theNewsApiAdapter.fetchNews();

        assertNotNull(fetchedNews);
    }

    @Test
    void testGetServiceName() {
        String serviceName = theNewsApiAdapter.getServiceName();
        assertEquals("The News API", serviceName);
    }

}
