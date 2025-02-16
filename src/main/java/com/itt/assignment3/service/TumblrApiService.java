package com.itt.assignment3.service;

import com.itt.assignment3.constant.HttpMethod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public class TumblrApiService {

    private static final String TUMBLR_API_RESPONSE_PREFIX = "var tumblr_api_read = ";

    public Optional<String> getResponse(final String apiUrl) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod.valueOf(HttpMethod.GET));

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String jsonString = response
                    .toString()
                    .replace(TUMBLR_API_RESPONSE_PREFIX, "")
                    .trim();
            return Optional.of(jsonString);
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
        return Optional.empty();
    }

}
