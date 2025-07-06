package com.intimetec.newsaggregation.eventlistener;

import com.intimetec.newsaggregation.dto.EmailNotificationPayload;
import com.intimetec.newsaggregation.dto.event.NewsReportedEvent;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.notification.email.EmailNotificationService;
import com.intimetec.newsaggregation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsReportedEventListener {

    private final UserRepository userRepository;
    private final EmailNotificationService emailNotificationService;

    @EventListener
    public void onNewsReportedEvent(NewsReportedEvent event) {
        final User adminUser = getAdminUser();

        final EmailNotificationPayload emailNotificationPayload = new EmailNotificationPayload();
        emailNotificationPayload.setSubject("A news has been reported");
        emailNotificationPayload.setBody("User " + event.getReportedByEmail() + " has reported the news with id: " + event.getNewsId() + ". News URL: " + event.getNewsUrl());
        emailNotificationPayload.setRecipientEmailAddress(adminUser.getEmail());
        emailNotificationService.sendEmailNotification(emailNotificationPayload);
    }

    private User getAdminUser() {
        return userRepository
                .findAdminUser()
                .orElseThrow(() -> new BadRequestException("No admin user found to send email notification"));
    }

}
