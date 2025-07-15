package com.intimetec.newsaggregation.dto.request;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class ViewHeadlinesRequest {

    private LocalDate from;
    private LocalDate to;

}
