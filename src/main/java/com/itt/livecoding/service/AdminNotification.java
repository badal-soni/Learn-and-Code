package com.itt.livecoding.service;

public class AdminNotification implements NotificationService {

    private final String emailBody;

    public AdminNotification(String emailBody) {
        this.emailBody = emailBody;
    }

    @Override
    public void sendNotification() {
        try {
            System.out.println("Admin notification: " + emailBody);
            Thread.sleep(50);
        } catch (Exception e) {
            System.out.println("Failed to send admin notification");
        }
    }

}
