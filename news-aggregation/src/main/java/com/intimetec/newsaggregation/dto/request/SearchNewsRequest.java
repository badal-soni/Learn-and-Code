package com.intimetec.newsaggregation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SearchNewsRequest extends ViewHeadlinesRequest {

    @NotBlank
    private String searchQuery;
    private Boolean isAscendingOrder;

}
