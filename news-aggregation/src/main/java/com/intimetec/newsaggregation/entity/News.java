package com.intimetec.newsaggregation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "news")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News extends BaseEntity {

    @Column(nullable = false)
    private String headline;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private LocalDate publishedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "news_to_category",
            joinColumns = @JoinColumn(name = "news_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private List<NewsCategory> categories = new ArrayList<>();

    @OneToMany(
            mappedBy = "newsArticle",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<SavedArticle> savedNews = new ArrayList<>();

    @OneToMany(
            mappedBy = "news",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<NewsLikes> userInteractions = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.setTimestampsBeforeInsert();
    }

    @PreUpdate
    public void preUpdate() {
        this.setTimestampsBeforeUpdate();
    }

}
