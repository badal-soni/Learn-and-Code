package com.intimetec.newsaggregation.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class NewsConfigurationResponse {

    private String categoryName;
    private boolean isEnabled;
    private List<KeywordResponse> keywords;

}
