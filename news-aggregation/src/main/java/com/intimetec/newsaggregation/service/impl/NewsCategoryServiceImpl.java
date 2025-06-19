package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.dto.request.CreateCategoryRequest;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.service.NewsCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<String> listAllCategories() {
        return newsCategoryRepository
                .findAll()
                .stream()
                .map(NewsCategory::getCategoryName)
                .toList();
    }

}
