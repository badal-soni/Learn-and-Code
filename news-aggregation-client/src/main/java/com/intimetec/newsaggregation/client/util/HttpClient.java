package com.intimetec.newsaggregation.client.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpClient {

    private final ObjectMapper objectMapper;

    public HttpClient() {
        this.objectMapper = new ObjectMapper();
    }

    public <T> ApiResponse<T> get(String urlString, Map<String, String> headers, Class<T> responseType) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
        }

        int responseCode = connection.getResponseCode();
        System.out.println("GET Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder responseJson = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseJson.append(line);
                }
                // Deserialize the JSON into an ApiResponse object
                return objectMapper.readValue(responseJson.toString(),
                        objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, responseType));
            }
        } else {
            throw new Exception("GET request failed with response code: " + responseCode);
        }
    }

    public <T, R> ApiResponse<T> post(String urlString, R requestBody, Map<String, String> headers, Class<T> responseType) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
        }
        connection.setRequestProperty("Content-Type", "application/json");

        if (requestBody != null) {
            String jsonRequestBody = objectMapper.writeValueAsString(requestBody);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonRequestBody.getBytes());
                os.flush();
            }
        }

        int responseCode = connection.getResponseCode();
        System.out.println("POST Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder responseJson = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseJson.append(line);
                }
                // Deserialize the JSON into an ApiResponse object
                return objectMapper.readValue(responseJson.toString(),
                        objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, responseType));
            }
        } else {
            throw new Exception("POST request failed with response code: " + responseCode);
        }
    }

//    public <T> String handleApiResponse(String url, Class<T> responseType) throws Exception {
//        ApiResponse<T> apiResponse = get(url, null, responseType);
//
//        if (apiResponse.isSuccess()) {
//            // Assuming the data object contains an `id` field
//            T data = apiResponse.getData();
//            return "ID: " + objectMapper.convertValue(data, Map.class).get("id");
//        } else {
//            return "Error: " + apiResponse.getMessage();
//        }
//    }
}

