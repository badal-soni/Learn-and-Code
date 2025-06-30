package com.intimetec.newsaggregation.notification.email;

import com.intimetec.newsaggregation.constant.EmailConstants;
import com.intimetec.newsaggregation.dto.EmailNotificationPayload;
import com.intimetec.newsaggregation.dto.event.UserRegisteredEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GmailNotificationService implements EmailNotificationService {

    private final JavaMailSender javaMailSender;
    private final ExecutorService emailExecutorService = Executors.newSingleThreadExecutor();

    @Value("${spring.mail.username}")
    private String senderEmailAddress;

    @Override
    @Async("asyncExecutor")
    public void sendEmailNotification(EmailNotificationPayload emailNotificationPayload) {
        try {
            System.out.println("Sending single email");
            MimeMessage mimeMessage = buildMessage(emailNotificationPayload);
            emailExecutorService.execute(() -> javaMailSender.send(mimeMessage));
        } catch (Exception exception) {
            throw new RuntimeException("Failed to send email notification", exception);
        }
    }

    @Override
    @Async("asyncExecutor")
    public void sendEmailNotificationInBulk(List<EmailNotificationPayload> emailNotificationPayloads) {
        MimeMessage[] mimeMessages = new MimeMessage[emailNotificationPayloads.size()];
        int index = 0;
        for (EmailNotificationPayload emailNotificationPayload : emailNotificationPayloads) {
            mimeMessages[index++] = buildMessage(emailNotificationPayload);
        }
        System.out.println("Sending bulk email");
        emailExecutorService.execute(() -> javaMailSender.send(mimeMessages));
    }

    private MimeMessage buildMessage(EmailNotificationPayload emailNotificationPayload) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mail = new MimeMessageHelper(mimeMessage);
            mail.setFrom(senderEmailAddress);
            mail.setTo(emailNotificationPayload.getRecipientEmailAddress());
            mail.setText(emailNotificationPayload.getBody());
            mail.setSubject(emailNotificationPayload.getSubject());
            mail.setSentDate(new Date());
            return mimeMessage;
        } catch (MessagingException exception) {
            log.error(exception.getMessage());
            throw new RuntimeException("Something went wrong while generating the content to be sent");
        }
    }

//    @EventListener
    public void sendRegistrationEmail(UserRegisteredEvent userRegisteredEvent) {
        EmailNotificationPayload welcomeEmailPayload = new EmailNotificationPayload();
        welcomeEmailPayload.setSenderEmailAddress(senderEmailAddress);
        welcomeEmailPayload.setRecipientEmailAddress(userRegisteredEvent.getEmail());
        welcomeEmailPayload.setSubject(EmailConstants.WELCOME_EMAIL_SUBJECT);
        welcomeEmailPayload.setBody(String.format(EmailConstants.WELCOME_EMAIL_CONTENT, userRegisteredEvent.getUsername()));
        sendEmailNotification(welcomeEmailPayload);
    }

}
