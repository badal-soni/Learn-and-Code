package com.intimetec.newsaggregation.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "saved_articles")
@Data
public class SavedArticle extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "saved_by", nullable = false)
    private User savedBy;

    @ManyToOne
    @JoinColumn(name = "news_article", nullable = false)
    private News newsArticle;

}
