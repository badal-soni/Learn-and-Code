package com.intimetec.newsaggregation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DateRangeFilterCriteria extends CommonSearchCriteria {

    private LocalDate fromDate;
    private LocalDate toDate;

}
