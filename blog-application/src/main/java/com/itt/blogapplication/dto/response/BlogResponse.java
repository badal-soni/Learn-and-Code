package com.itt.blogapplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogResponse {

    private UUID id;
    private String title;
    private String author;
    private String content;
    private String createdAt;
    private String updatedAt;

}
