package com.intimetec.newsaggregation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedNewsResponse {

    private Long id;
    private String headline;
    private String description;
    private String source;
    private String url;
    private List<String> categories;

}
