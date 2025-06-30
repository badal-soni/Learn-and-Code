package com.intimetec.newsaggregation.client.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class NewsResponse {

    private Long id;
    private String headline;
    private String description;
    private String source;
    private String url;
    private boolean isHidden;
    private LocalDate publishedAt;
    private List<String> categories;

}
