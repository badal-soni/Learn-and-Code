package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.response.NewsResponse;
import com.intimetec.newsaggregation.entity.News;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.repository.NewsLikesRepository;
import com.intimetec.newsaggregation.repository.NewsReadHistoryRepository;
import com.intimetec.newsaggregation.repository.SavedArticleRepository;
import com.intimetec.newsaggregation.service.impl.NewsPreferencesServiceImpl;
import com.intimetec.newsaggregation.util.MockDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class NewsPreferencesServiceImplTest {

    @Mock
    private NewsReadHistoryRepository newsReadHistoryRepository;

    @Mock
    private NewsLikesRepository newsLikesRepository;

    @Mock
    private SavedArticleRepository savedArticleRepository;

    @InjectMocks
    private NewsPreferencesServiceImpl newsPreferencesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sortNewsByUserPreferences_shouldReturnEmptyListIfNewsListIsEmpty() {
        User user = MockDataCreator.createMockUser();
        List<NewsResponse> responses = newsPreferencesService.sortNewsByUserPreferences(user, List.of());
        assertTrue(responses.isEmpty());
    }

    @Test
    void sortCategoryNews_shouldReturnEmptyListIfCategoryNewsListIsEmpty() {
        User user = MockDataCreator.createMockUser();
        List<NewsResponse> responses = newsPreferencesService.sortCategoryNews(user, List.of());
        assertTrue(responses.isEmpty());
    }

    @Test
    void sortCategoryNews_shouldSortCategoryNewsBasedOnPreferences() {
        User user = MockDataCreator.createMockUser();
        News news = MockDataCreator.createMockNews();
        news.setReadHistory(List.of(MockDataCreator.createMockNewsReadHistory()));
        news.setUserInteractions(List.of(MockDataCreator.createMockNewsLikes()));
        news.setSavedNews(List.of(MockDataCreator.createMockSavedArticle()));
        when(newsReadHistoryRepository.findAllIdsByReadBy(user)).thenReturn(List.of(news.getId()));
        TreeSet<Long> likedNewsIds = new TreeSet<>();
        likedNewsIds.add(news.getId());
        when(newsLikesRepository.findAllLikedNewsIds(user.getId())).thenReturn(likedNewsIds);
        when(savedArticleRepository.findAllNewsIdsBySavedById(user.getId())).thenReturn(List.of(news.getId()));
        List<NewsResponse> responses = newsPreferencesService.sortCategoryNews(user, List.of(news));
        assertEquals(1, responses.size());
    }

}