package com.intimetec.newsaggregation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "news_read_history")
@Getter
@Setter
public class NewsReadHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(
            name = "news_id",
            nullable = false
    )
    private News news;

    @ManyToOne
    @JoinColumn(
            name = "read_by",
            nullable = false
    )
    private User readBy;

}
