package com.intimetec.newsaggregation.dto.event;

import lombok.Data;

@Data
public class NewsReportedEvent {

    private Long newsId;
    private String newsUrl;
    private String reportedByEmail;
    private String reportReason;

}
