package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.request.CreateCategoryRequest;
import com.intimetec.newsaggregation.dto.response.NewsCategoryResponse;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.service.impl.NewsCategoryServiceImpl;
import com.intimetec.newsaggregation.util.MockDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NewsCategoryServiceImplTest {

    @Mock
    private NewsCategoryRepository newsCategoryRepository;

    @InjectMocks
    private NewsCategoryServiceImpl newsCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory_shouldThrowExceptionIfCategoryExists() {
        CreateCategoryRequest request = MockDataCreator.createMockCreateCategoryRequest();
        when(newsCategoryRepository.existsByCategoryName(request.getCategoryName().toLowerCase())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> newsCategoryService.createCategory(request));
    }

    @Test
    void createCategory_shouldSaveCategoryIfNotExists() {
        CreateCategoryRequest request = MockDataCreator.createMockCreateCategoryRequest();
        when(newsCategoryRepository.existsByCategoryName(request.getCategoryName().toLowerCase())).thenReturn(false);
        newsCategoryService.createCategory(request);
        verify(newsCategoryRepository, times(1)).save(any(NewsCategory.class));
    }

    @Test
    void getAllCategories_shouldReturnMappedResponse() {
        NewsCategory newsCategory = MockDataCreator.createMockNewsCategory();
        when(newsCategoryRepository.findAll()).thenReturn(List.of(newsCategory));
        NewsCategoryResponse response = newsCategoryService.getAllCategories();
        assertEquals(1, response.getNewsCategoryDetails().size());
    }

    @Test
    void hideNewsCategories_shouldHideCategories() {
        List<String> categories = List.of("mockCategory");
        newsCategoryService.hideNewsCategories(categories);
        verify(newsCategoryRepository, times(1)).hideNews(categories);
    }

    @Test
    void unHideNewsCategories_shouldUnhideCategories() {
        List<String> categories = List.of("mockCategory");
        newsCategoryService.unHideNewsCategories(categories);
        verify(newsCategoryRepository, times(1)).unhideNews(categories);
    }

    @Test
    void getAllHiddenCategories_shouldReturnHiddenCategories() {
        NewsCategory newsCategory = MockDataCreator.createMockNewsCategory();
        when(newsCategoryRepository.findAllByIsHiddenTrue()).thenReturn(List.of(newsCategory));
        NewsCategoryResponse response = newsCategoryService.getAllHiddenCategories();
        assertEquals(1, response.getNewsCategoryDetails().size());
    }

    @Test
    void getAllUnHiddenCategories_shouldReturnUnhiddenCategories() {
        NewsCategory newsCategory = MockDataCreator.createMockNewsCategory();
        when(newsCategoryRepository.findAllByIsHiddenFalse()).thenReturn(List.of(newsCategory));
        NewsCategoryResponse response = newsCategoryService.getAllUnHiddenCategories();
        assertEquals(1, response.getNewsCategoryDetails().size());
    }
}