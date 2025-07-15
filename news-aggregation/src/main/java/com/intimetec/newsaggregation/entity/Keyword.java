package com.intimetec.newsaggregation.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "keywords")
@Getter
@Setter
@EqualsAndHashCode(of = {"keyword", "parentCategory"}, callSuper = true)
public class Keyword extends BaseEntity {

    @Column(
            nullable = false,
            length = 50
    )
    private String keyword;

    @Column(
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "parent_category")
    private NewsCategory parentCategory;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_keyword",
            joinColumns = @JoinColumn(name = "keyword_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>();

}
