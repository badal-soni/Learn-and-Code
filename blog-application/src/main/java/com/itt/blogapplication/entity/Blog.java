package com.itt.blogapplication.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Blog {

    private UUID id;
    private String title;
    private String author;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
