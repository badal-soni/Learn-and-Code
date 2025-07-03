package com.intimetec.newsaggregation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "blocked_keyword")
@Getter
@Setter
public class BlockedKeyword extends BaseEntity {

    @Column(nullable = false)
    private String blockedKeyword;

    @PrePersist
    public void prePersist() {
        this.blockedKeyword = this.blockedKeyword.toLowerCase();
    }

    @PreUpdate
    public void preUpdate() {
        this.blockedKeyword = this.blockedKeyword.toLowerCase();
    }

}
