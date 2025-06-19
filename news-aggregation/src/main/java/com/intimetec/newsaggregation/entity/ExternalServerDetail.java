package com.intimetec.newsaggregation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    @Column
    private LocalDate lastAccessedDate;

    @Column(nullable = false)
    private String requestUri; // todo: this is the complete url with cna be used directly for making requests (append &apiKey= or &api_token=)

//    @Column(nullable = false)
//    private String apiKeyPrefix; // apiKey&= or api_token&=

    @PrePersist
    public void preInsert() {
        this.serverName = this.serverName.toUpperCase();
        this.setTimestampsBeforeInsert();
        this.setTimestampsBeforeUpdate();
    }

    @PreUpdate
    public void preUpdate() {
        this.setTimestampsBeforeUpdate();
    }

}
