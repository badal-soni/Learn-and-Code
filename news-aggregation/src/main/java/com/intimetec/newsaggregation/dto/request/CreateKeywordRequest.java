package com.intimetec.newsaggregation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateKeywordRequest {

    @NotBlank
    private String keyword;
    private String parentCategory;

}
