package com.intimetec.newsaggregation.dto.response;

import lombok.Data;

@Data
public class KeywordResponse {

    private Long keywordId;
    private String keyword;
    private String parentCategory;
    private boolean isEnabled;

}
