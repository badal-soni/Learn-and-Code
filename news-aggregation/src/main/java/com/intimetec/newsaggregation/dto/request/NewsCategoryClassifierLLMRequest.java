package com.intimetec.newsaggregation.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class NewsCategoryClassifierLLMRequest {

    private String text;
    private List<String> categories;

}
