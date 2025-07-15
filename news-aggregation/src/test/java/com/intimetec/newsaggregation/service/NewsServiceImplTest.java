package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.constant.NewsInteractionType;
import com.intimetec.newsaggregation.dto.Keywords;
import com.intimetec.newsaggregation.dto.request.SearchNewsRequest;
import com.intimetec.newsaggregation.dto.request.ViewHeadlinesRequest;
import com.intimetec.newsaggregation.dto.response.NewsResponse;
import com.intimetec.newsaggregation.entity.News;
import com.intimetec.newsaggregation.entity.NewsLikes;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.repository.*;
import com.intimetec.newsaggregation.service.impl.NewsServiceImpl;
import com.intimetec.newsaggregation.util.MockDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NewsServiceImplTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsLikesRepository newsLikesRepository;

    @Mock
    private NewsCategoryRepository newsCategoryRepository;

    @Mock
    private SavedArticleRepository savedArticleRepository;

    @Mock
    private NewsReadHistoryRepository newsReadHistoryRepository;

    @Mock
    private NewsPreferencesService newsPreferencesService;

    @InjectMocks
    private NewsServiceImpl newsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toggleLikeStatus_shouldToggleLike() {
        User user = MockDataCreator.createMockUser();
        News news = MockDataCreator.createMockNews();
        NewsLikes newsLikes = MockDataCreator.createMockNewsLikes();
        when(newsRepository.findById(news.getId())).thenReturn(Optional.of(news));
        when(newsLikesRepository.findByNewsIdAndInteractionUserIdAndInteractionType(news.getId(), user.getId(), NewsInteractionType.LIKE))
                .thenReturn(Optional.of(newsLikes));
        newsService.toggleLikeStatus(news.getId(), user);
        verify(newsLikesRepository, times(1)).deleteById(newsLikes.getId());
    }

    @Test
    void getHeadlines_shouldReturnSortedHeadlines() {
        User user = MockDataCreator.createMockUser();
        ViewHeadlinesRequest request = new ViewHeadlinesRequest();
        request.setFrom(LocalDate.now().minusDays(1));
        request.setTo(LocalDate.now());
        News news = MockDataCreator.createMockNews();
        when(newsRepository.findAllByPublishedAtBetweenAndIsHiddenFalse(request.getFrom(), request.getTo()))
                .thenReturn(List.of(news));
        when(newsPreferencesService.sortNewsByUserPreferences(user, List.of(news)))
                .thenReturn(List.of(MockDataCreator.createMockNewsResponse()));
        List<NewsResponse> responses = newsService.getHeadlines(user, request);
        assertEquals(1, responses.size());
    }

    @Test
    void getHeadlinesUnderCategory_shouldReturnSortedCategoryHeadlines() {
        User user = MockDataCreator.createMockUser();
        ViewHeadlinesRequest request = new ViewHeadlinesRequest();
        request.setFrom(LocalDate.now().minusDays(1));
        request.setTo(LocalDate.now());
        News news = MockDataCreator.createMockNews();
        when(newsCategoryRepository.existsByCategoryName("mockCategory")).thenReturn(true);
        when(newsRepository.findAllByCategoriesCategoryNameAndPublishedAtBetweenAndIsHiddenFalse("mockCategory", request.getFrom(), request.getTo()))
                .thenReturn(List.of(news));
        when(newsPreferencesService.sortCategoryNews(user, List.of(news)))
                .thenReturn(List.of(MockDataCreator.createMockNewsResponse()));
        List<NewsResponse> responses = newsService.getHeadlinesUnderCategory(user, "mockCategory", request);
        assertEquals(1, responses.size());
    }

    @Test
    void saveNews_shouldSaveNews() {
        User user = MockDataCreator.createMockUser();
        News news = MockDataCreator.createMockNews();
        when(savedArticleRepository.existsByNewsArticleIdAndSavedById(news.getId(), user.getId())).thenReturn(false);
        when(newsRepository.findById(news.getId())).thenReturn(Optional.of(news));
        newsService.saveNews(news.getId(), user);
        verify(savedArticleRepository, times(1)).save(any());
    }

    @Test
    void unSaveNews_shouldUnSaveNews() {
        User user = MockDataCreator.createMockUser();
        News news = MockDataCreator.createMockNews();
        when(savedArticleRepository.existsByNewsArticleIdAndSavedById(news.getId(), user.getId())).thenReturn(true);
        newsService.unSaveNews(news.getId(), user);
        verify(savedArticleRepository, times(1)).deleteBySavedByAndNewsArticleId(user.getId(), news.getId());
    }

    @Test
    void getTodayHeadline_shouldReturnTodayHeadlines() {
        User user = MockDataCreator.createMockUser();
        News news = MockDataCreator.createMockNews();
        when(newsRepository.findAllByPublishedAtAndIsHiddenFalse(LocalDate.now())).thenReturn(List.of(news));
        when(newsPreferencesService.sortNewsByUserPreferences(user, List.of(news)))
                .thenReturn(List.of(MockDataCreator.createMockNewsResponse()));
        List<NewsResponse> responses = newsService.getTodayHeadline(user);
        assertEquals(1, responses.size());
    }

    @Test
    void searchNews_shouldReturnSortedNews() {
        SearchNewsRequest request = new SearchNewsRequest();
        request.setSearchQuery("mock");
        News news = MockDataCreator.createMockNews();
        when(newsRepository.findAllNewsMatchesHeadlineOrDescriptionAndHiddenFalse(request.getSearchQuery()))
                .thenReturn(List.of(news));
        List<NewsResponse> responses = newsService.searchNews(request);
        assertEquals(1, responses.size());
    }

    @Test
    void getHiddenNews_shouldReturnHiddenNews() {
        News news = MockDataCreator.createMockNews();
        when(newsRepository.findAllByIsHiddenTrue()).thenReturn(List.of(news));
        List<NewsResponse> responses = newsService.getHiddenNews();
        assertEquals(1, responses.size());
    }

    @Test
    void getNewsWithKeywords_shouldReturnNewsWithKeywords() {
        Keywords keywords = new Keywords();
        keywords.setKeywords(List.of("mock"));
        News news = MockDataCreator.createMockNews();
        when(newsRepository.findAllHavingKeywords(anyString())).thenReturn(List.of(news));
        List<NewsResponse> responses = newsService.getNewsWithKeywords(keywords);
        assertEquals(1, responses.size());
    }

    @Test
    void getNewsById_shouldReturnNewsById() {
        User user = MockDataCreator.createMockUser();
        News news = MockDataCreator.createMockNews();
        when(newsRepository.findById(news.getId())).thenReturn(Optional.of(news));
        when(newsReadHistoryRepository.existsByNewsIdAndReadById(news.getId(), user.getId())).thenReturn(false);
        NewsResponse response = newsService.getNewsById(news.getId(), user);
        assertEquals(news.getHeadline(), response.getHeadline());
    }
}