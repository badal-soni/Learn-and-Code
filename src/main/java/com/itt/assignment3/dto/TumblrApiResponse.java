package com.itt.assignment3.dto;

import org.json.JSONArray;
import org.json.JSONObject;

public record TumblrApiResponse(
        JSONArray posts,
        String title,
        String name,
        String description,
        int totalPosts
) {

    public void logToConsole() {
        System.out.println("title: " + title);
        System.out.println("name: " + name);
        System.out.println("description: " + description);
        System.out.println("no of post: " + totalPosts);

        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);
            System.out.println(i + 1);

            if (post.has("photos")) {
                JSONArray photos = post.getJSONArray("photos");
                for (int j = 0; j < photos.length(); j++) {
                    JSONObject photo = photos.getJSONObject(j);
                    String imageUrl = photo.optString("photo-url-1280", "No Image URL");
                    System.out.println("   " + imageUrl);
                }
            } else {
                System.out.println("   No images found in this post.");
            }
        }
    }

}
