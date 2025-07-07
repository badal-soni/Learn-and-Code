package com.intimetec.newsaggregation.eventlistener;

import com.intimetec.newsaggregation.constant.EmailConstants;
import com.intimetec.newsaggregation.constant.Messages;
import com.intimetec.newsaggregation.dto.EmailNotificationPayload;
import com.intimetec.newsaggregation.dto.event.NewsReportedEvent;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.exception.NotFoundException;
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
        sendEmailNotification(event);
    }

    private void sendEmailNotification(NewsReportedEvent event) {
        final User adminUser = getAdminUser();
        final EmailNotificationPayload emailNotificationPayload = new EmailNotificationPayload();
        emailNotificationPayload.setSubject(EmailConstants.NEWS_REPORTED_SUBJECT);
        final String emailContent = String.format(
                EmailConstants.NEWS_REPORTED_CONTENT,
                event.getReportedByEmail(),
                event.getNewsId(),
                event.getNewsUrl(),
                event.getReportReason()
        );
        emailNotificationPayload.setBody(emailContent);
        emailNotificationPayload.setRecipientEmailAddress(adminUser.getEmail());
        emailNotificationService.sendEmailNotification(emailNotificationPayload);
    }

    private User getAdminUser() {
        return userRepository
                .findAdminUser()
                .orElseThrow(() -> new NotFoundException(Messages.ADMIN_USER_NOT_FOUND));
    }

}
