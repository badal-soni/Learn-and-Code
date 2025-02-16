package com.itt.assignment3.util;

import com.itt.assignment3.dto.TumblrApiResponse;
import org.json.JSONObject;

public class Parser {

    public TumblrApiResponse parse(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);

        JSONObject tumbleLog = jsonObject.getJSONObject("tumblelog");
        return new TumblrApiResponse(
                jsonObject.getJSONArray("posts"),
                tumbleLog.optString("title", "N/A"),
                tumbleLog.optString("name", "N/A"),
                tumbleLog.optString("description", "N/A"),
                jsonObject.optInt("posts-total", 0)
        );
    }

}
