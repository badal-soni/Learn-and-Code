package com.intimetec.newsaggregation.client.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchNewsRequest {

    private LocalDate fromDate;
    private LocalDate toDate;
    private String searchQuery;
    private String sortBy;
    private int pageIndex;
    private int pageSize;
    private boolean isAscendingOrder;

}
