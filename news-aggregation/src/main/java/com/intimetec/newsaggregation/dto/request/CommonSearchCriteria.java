package com.intimetec.newsaggregation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommonSearchCriteria {

    @NotBlank
    private String searchQuery;
    private String sortBy; // likes or dislikes
    private boolean isAscendingOrder;
    private int pageIndex;
    private int pageSize;

}
