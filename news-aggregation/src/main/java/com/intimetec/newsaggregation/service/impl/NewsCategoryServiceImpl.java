package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.dto.request.CreateCategoryRequest;
import com.intimetec.newsaggregation.dto.response.NewsCategoryResponse;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.service.NewsCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsCategoryServiceImpl implements NewsCategoryService {

    private final NewsCategoryRepository newsCategoryRepository;

    @Override
    public void createCategory(CreateCategoryRequest createCategoryRequest) {
        final String categoryName = createCategoryRequest.getCategoryName().toLowerCase();
        if (newsCategoryRepository.existsByCategoryName(categoryName)) {
            throw new BadRequestException("Category " + categoryName + " already exists");
        }
        NewsCategory category = new NewsCategory();
        category.setCategoryName(categoryName);
        newsCategoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public NewsCategoryResponse getAllCategories() {
        return mapToNewsCategoryResponse(newsCategoryRepository.findAll());
    }

    @Override
    public void hideNewsCategories(List<String> newsCategories) {
        newsCategoryRepository.hideNews(newsCategories);
    }

    @Override
    public void unHideNewsCategories(List<String> newsCategories) {
        newsCategoryRepository.unhideNews(newsCategories);
    }

    @Override
    public NewsCategoryResponse getAllHiddenCategories() {
        return mapToNewsCategoryResponse(newsCategoryRepository.findAllByIsHiddenTrue());
    }

    private NewsCategoryResponse mapToNewsCategoryResponse(List<NewsCategory> newsCategories) {
        final List<NewsCategoryResponse.NewsCategoryDetail> newsCategoryDetails = new ArrayList<>();
        newsCategories
                .forEach(newsCategory -> {
                    NewsCategoryResponse.NewsCategoryDetail newsCategoryDetail = new NewsCategoryResponse.NewsCategoryDetail(
                            newsCategory.getCategoryName(),
                            newsCategory.isHidden()
                    );
                    newsCategoryDetails.add(newsCategoryDetail);
                });
        NewsCategoryResponse newsCategoryResponse = new NewsCategoryResponse();
        newsCategoryResponse.setNewsCategoryDetails(newsCategoryDetails);
        return newsCategoryResponse;
    }

}
