package com.intimetec.newsaggregation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {

    private Long id;
    private String headline;
    private String description;
    private String source;
    private String url;
    private boolean isHidden;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public LocalDate publishedAt;
    private List<String> categories;

}
