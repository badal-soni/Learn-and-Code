package com.intimetec.newsaggregation.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class NewsCategoryResponse {

    public record NewsCategoryDetail(
            String categoryName,
            boolean isHidden
    ) {}

    private List<NewsCategoryDetail> newsCategoryDetails;

}
