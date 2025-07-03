package com.intimetec.newsaggregation.notification.email;

import com.intimetec.newsaggregation.constant.Messages;
import com.intimetec.newsaggregation.dto.EmailNotificationPayload;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GmailNotificationService implements EmailNotificationService {

    private final JavaMailSender javaMailSender;
    private final ThreadPoolTaskExecutor asyncExecutor;

    @Value("${spring.mail.username}")
    private String senderEmailAddress;

    @Override
    @Async("asyncExecutor")
    public void sendEmailNotification(EmailNotificationPayload emailNotificationPayload) {
        try {
            MimeMessage mimeMessage = buildMessage(emailNotificationPayload);
            log.debug("Sending single email to: {}", emailNotificationPayload.getRecipientEmailAddress());
            asyncExecutor.execute(() -> javaMailSender.send(mimeMessage));
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
        log.debug("Sending bulk email");
        asyncExecutor.execute(() -> javaMailSender.send(mimeMessages));
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
            throw new RuntimeException(Messages.SOMETHING_WENT_WRONG_EMAIL);
        }
    }

}
