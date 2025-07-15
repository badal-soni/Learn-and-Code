package com.intimetec.newsaggregation.dto;

import lombok.Data;

@Data
public class EmailNotificationPayload {

    private String senderEmailAddress;
    private String recipientEmailAddress;
    private String subject;
    private String body;

}
