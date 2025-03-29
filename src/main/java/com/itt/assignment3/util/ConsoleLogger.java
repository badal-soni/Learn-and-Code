package com.itt.assignment3.util;

import com.itt.assignment3.dto.TumblrApiResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConsoleLogger {

    public static void logTumblrApiResponse(TumblrApiResponse response) {
        System.out.println("title: " + response.title());
        System.out.println("name: " + response.name());
        System.out.println("description: " + response.description());
        System.out.println("no of post: " + response.totalPosts());

        final JSONArray posts = response.posts();

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
