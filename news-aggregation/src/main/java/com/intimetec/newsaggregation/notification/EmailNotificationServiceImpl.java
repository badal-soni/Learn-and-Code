package com.intimetec.newsaggregation.notification;

import com.intimetec.newsaggregation.dto.EmailNotificationPayload;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmailNotification(EmailNotificationPayload emailNotificationPayload) {
        try {
            MimeMessage mimeMessage = buildMessage(emailNotificationPayload);
            javaMailSender.send(mimeMessage);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to send email notification", exception);
        }
    }

    @Override
    public void sendEmailNotificationInBulk(List<EmailNotificationPayload> emailNotificationPayloads) {
        MimeMessage[] mimeMessages = new MimeMessage[emailNotificationPayloads.size()];
        int index = 0;
        for (EmailNotificationPayload emailNotificationPayload : emailNotificationPayloads) {
            mimeMessages[index++] = buildMessage(emailNotificationPayload);
        }
        System.out.println("Sending bulk emails");
        javaMailSender.send(mimeMessages);
    }

    private MimeMessage buildMessage(EmailNotificationPayload emailNotificationPayload) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mail = new MimeMessageHelper(mimeMessage);
            mail.setFrom(emailNotificationPayload.getSubject());
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

}
