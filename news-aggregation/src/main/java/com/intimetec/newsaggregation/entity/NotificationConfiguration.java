package com.intimetec.newsaggregation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notifications_configurations")
@Getter
@Setter
public class NotificationConfiguration extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "news_category")
    private NewsCategory newsCategory;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isEnabled;

}
