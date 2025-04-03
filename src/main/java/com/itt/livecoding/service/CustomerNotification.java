package com.itt.livecoding.service;

public class CustomerNotification implements NotificationService {

    private final String customerEmail;
    private final String emailBody;

    public CustomerNotification(
            String customerEmail,
            String emailBody
    ) {
        this.customerEmail = customerEmail;
        this.emailBody = emailBody;
    }

    @Override
    public void sendNotification() {
        try {
            System.out.println("Sending email to " + this.customerEmail + ": " + emailBody);
            Thread.sleep(100);
        } catch (Exception e) {
            System.out.println("Failed to send customer notification");
        }
    }

}
