package com.intimetec.newsaggregation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonNewsApiResponse {

    private String headline;
    private String description;
    private String source;
    private String url;
    private LocalDate publishedAt;
    private List<String> categories;

}
