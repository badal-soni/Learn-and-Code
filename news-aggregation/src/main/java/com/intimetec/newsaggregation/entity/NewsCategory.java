package com.intimetec.newsaggregation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "news_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "categoryName"})
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

    @Column(
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT FALSE"
    )
    private boolean isHidden;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
    private List<News> news = new ArrayList<>();

    @OneToMany(mappedBy = "newsCategory", fetch = FetchType.EAGER)
    private List<NotificationConfiguration> notificationConfigurations = new ArrayList<>();

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.EAGER)
    private List<Keyword> keywords = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.isHidden = false;
    }

}
