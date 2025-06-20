package com.intimetec.newsaggregation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "news_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(
            name = "name",
            nullable = false,
            unique = true
    )
    private String categoryName;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
    private List<News> news = new ArrayList<>();

    @OneToMany(mappedBy = "newsCategory", fetch = FetchType.EAGER)
    private List<NotificationConfiguration> notificationConfigurations = new ArrayList<>();

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.EAGER)
    private List<Keyword> keywords = new ArrayList<>();

    @PrePersist
    public void prePersist() {
//        this.setTimestampsBeforeInsert();
    }

    @PreUpdate
    public void preUpdate() {
//        this.setTimestampsBeforeUpdate();
    }

}
