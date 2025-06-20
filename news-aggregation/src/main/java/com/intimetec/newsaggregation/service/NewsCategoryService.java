package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.request.CreateCategoryRequest;

import java.util.List;

public interface NewsCategoryService {

    void createCategory(CreateCategoryRequest createCategoryRequest);

    List<String> listAllCategories();

}
