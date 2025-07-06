package com.intimetec.newsaggregation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "external_server_detail")
@Getter
@Setter
public class ExternalServerDetail extends BaseEntity {

    @Column(
            nullable = false,
            unique = true,
            updatable = false
    )
    private String serverName;

    @Column(nullable = false)
    private String apiKey;

    @Column(
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private boolean isActive;

    @Column
    private LocalDate lastAccessedDate;

    @Column
    private LocalDateTime lastFailedTime;

    @Column(nullable = false)
    private String requestUri;

    @PrePersist
    public void preInsert() {
        this.serverName = this.serverName.toUpperCase();
    }

}
