package com.intimetec.newsaggregation.notification.email;

import com.intimetec.newsaggregation.dto.EmailNotificationPayload;

import java.util.List;

public interface EmailNotificationService {

    void sendEmailNotification(EmailNotificationPayload emailNotificationPayload);

    void sendEmailNotificationInBulk(List<EmailNotificationPayload> emailNotificationPayloads);

}
