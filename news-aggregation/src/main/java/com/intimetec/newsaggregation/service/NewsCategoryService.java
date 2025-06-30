package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.request.CreateCategoryRequest;
import com.intimetec.newsaggregation.dto.response.NewsCategoryResponse;

import java.util.List;

public interface NewsCategoryService {

    void createCategory(CreateCategoryRequest createCategoryRequest);

    NewsCategoryResponse getAllCategories();

    void hideNewsCategories(List<String> newsCategories);

    void unHideNewsCategories(List<String> newsCategories);

    NewsCategoryResponse getAllHiddenCategories();

}
