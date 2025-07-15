package com.intimetec.newsaggregation.entity;

import com.intimetec.newsaggregation.constant.NotificationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "notification")
@Data
public class Notification extends BaseEntity {

    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean hasRead;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(nullable = false)
    private Long newsId;

    @ManyToOne
    @JoinColumn(
            name = "receiver_id",
            nullable = false
    )
    private User receiver;

}
