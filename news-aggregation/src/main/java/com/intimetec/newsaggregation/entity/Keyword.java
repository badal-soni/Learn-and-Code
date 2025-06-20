package com.intimetec.newsaggregation.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "keywords")
@Data
public class Keyword extends BaseEntity {

    @Column(length = 50)
    private String keyword;

    @Column(
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "parent_category")
    private NewsCategory parentCategory;

    @ManyToMany(mappedBy = "keywords", fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.setTimestampsBeforeInsert();
    }

    @PreUpdate
    public void preUpdate() {
        this.setTimestampsBeforeUpdate();
    }

}
// todo: keyword is unique inside a category i.e. two or more categories can have common keyword
//       but one category cannot have same keyword
