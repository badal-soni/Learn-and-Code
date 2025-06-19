package com.intimetec.newsaggregation.dto.response;

import java.time.LocalDate;
import java.util.List;

// News API response does not returns the news category
public class NewsApiResponse {

    private String title;
    private String description;
    private String source;
    private String url;
    private LocalDate publishedAt;
    private List<String> categories;

}
