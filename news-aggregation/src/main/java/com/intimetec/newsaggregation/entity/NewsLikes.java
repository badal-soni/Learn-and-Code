package com.intimetec.newsaggregation.entity;

import com.intimetec.newsaggregation.constant.NewsInteractionType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "news_likes")
@Data
public class NewsLikes extends BaseEntity {

    @ManyToOne
    @JoinColumn(
            name = "news_id",
            nullable = false
    )
    private News news;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User interactionUser;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NewsInteractionType interactionType;

}
