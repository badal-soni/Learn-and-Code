package com.itt.assignment3.dto;

import org.json.JSONArray;

public record TumblrApiResponse(
        JSONArray posts,
        String title,
        String name,
        String description,
        int totalPosts
) {

}
