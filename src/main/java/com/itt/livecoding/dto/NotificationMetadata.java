package com.itt.livecoding.dto;

import com.itt.livecoding.constant.Role;

public class NotificationMetadata {

    public Role receiverRole;
    public String receiverEmail;
    public String notificationBody;

    public NotificationMetadata(
            Role receiverRole,
            String receiverEmail,
            String notificationBody
    ) {
        this.receiverRole = receiverRole;
        this.receiverEmail = receiverEmail;
        this.notificationBody = notificationBody;
    }

}
