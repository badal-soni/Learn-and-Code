package com.intimetec.newsaggregation.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ViewHeadlinesRequest {

    private LocalDate from;
    private LocalDate to;

}
