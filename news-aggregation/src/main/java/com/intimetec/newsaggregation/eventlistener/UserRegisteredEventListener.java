package com.intimetec.newsaggregation.eventlistener;

import com.intimetec.newsaggregation.constant.EmailConstants;
import com.intimetec.newsaggregation.dto.EmailNotificationPayload;
import com.intimetec.newsaggregation.dto.event.UserRegisteredEvent;
import com.intimetec.newsaggregation.notification.email.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisteredEventListener {

    private final EmailNotificationService emailNotificationService;

    @Value("${spring.mail.username}")
    private String senderEmailAddress;

    @EventListener
    public void sendRegistrationEmail(UserRegisteredEvent userRegisteredEvent) {
        EmailNotificationPayload welcomeEmailPayload = new EmailNotificationPayload();
        welcomeEmailPayload.setSenderEmailAddress(senderEmailAddress);
        welcomeEmailPayload.setRecipientEmailAddress(userRegisteredEvent.getEmail());
        welcomeEmailPayload.setSubject(EmailConstants.WELCOME_EMAIL_SUBJECT);
        welcomeEmailPayload.setBody(String.format(EmailConstants.WELCOME_EMAIL_CONTENT, userRegisteredEvent.getUsername()));
        emailNotificationService.sendEmailNotification(welcomeEmailPayload);
    }

}
