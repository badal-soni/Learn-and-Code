package com.itt.livecoding.factory;

import com.itt.livecoding.constant.Role;
import com.itt.livecoding.dto.NotificationMetadata;
import com.itt.livecoding.service.AdminNotification;
import com.itt.livecoding.service.CustomerNotification;
import com.itt.livecoding.service.NotificationService;

public class NotificationFactory {

    public static NotificationService getInstance(NotificationMetadata notificationMetadata) {
        if (notificationMetadata.receiverRole.equals(Role.CUSTOMER)) {
            return new CustomerNotification(notificationMetadata.receiverEmail, notificationMetadata.notificationBody);
        }
        return new AdminNotification(notificationMetadata.notificationBody);
    }

}
