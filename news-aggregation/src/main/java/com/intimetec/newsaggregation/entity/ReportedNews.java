package com.intimetec.newsaggregation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reported_news")
@Getter
@Setter
public class ReportedNews extends BaseEntity {

    @Column(nullable = false)
    private String reportReason;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "reported_news"
    )
    private News reportedNews;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "reported_by"
    )
    private User reportedBy;

}