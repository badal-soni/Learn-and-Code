package com.intimetec.newsaggregation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportNewsArticleRequest {

    @NotBlank
    private String reportReason;

}
