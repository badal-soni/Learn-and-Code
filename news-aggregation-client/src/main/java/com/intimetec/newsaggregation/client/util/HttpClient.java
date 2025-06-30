package com.intimetec.newsaggregation.client.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpClient {

    private final ObjectMapper objectMapper;

    public HttpClient() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
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

    public <T> ApiResponse<List<T>> getList(String urlString, Map<String, String> headers, Class<T> responseType) throws Exception {
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
                return objectMapper.readValue(
                        responseJson.toString(),
                        objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, objectMapper.getTypeFactory().constructCollectionType(List.class, responseType))
                );
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
                return objectMapper.readValue(responseJson.toString(),
                        objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, responseType));
            }
        } else {
            throw new Exception("POST request failed with response code: " + responseCode);
        }
    }

    public <T, R> ApiResponse<List<T>> postList(String urlString, R requestBody, Map<String, String> headers, Class<T> responseType) throws Exception {
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
                return objectMapper.readValue(responseJson.toString(),
                        objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, responseType));
            }
        } else {
            throw new Exception("POST request failed with response code: " + responseCode);
        }
    }

    public <T, R> ApiResponse<T> put(String urlString, R requestBody, Map<String, String> headers, Class<T> responseType) throws Exception {
        return executeRequest("PUT", urlString, requestBody, headers, responseType);
    }

    public ApiResponse<Void> delete(String urlString, Map<String, String> headers) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
        }

        int responseCode = connection.getResponseCode();
        System.out.println("DELETE Response Code: " + responseCode);

        return processResponse(connection, Void.class);
    }

    private <T, R> ApiResponse<T> executeRequest(String method, String urlString, R requestBody, Map<String, String> headers, Class<T> responseType) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
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
        System.out.println(method + " Response Code: " + responseCode);

        return processResponse(connection, responseType);
    }

    private <T> ApiResponse<T> processResponse(HttpURLConnection connection, Class<T> responseType) throws Exception {
        int responseCode = connection.getResponseCode();
        String httpStatus = connection.getResponseMessage();
        boolean success = (responseCode >= 200 && responseCode < 300);

        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setHttpStatus(httpStatus);
        apiResponse.setSuccess(success);

        if (success) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder responseJson = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseJson.append(line);
                }
                if (!responseType.equals(Void.class)) {
                    T data = objectMapper.readValue(responseJson.toString(), responseType);
                    apiResponse.setData(data);
                }
            }
            apiResponse.setMessage("Request was successful");
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                StringBuilder errorJson = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    errorJson.append(line);
                }
                apiResponse.setMessage(errorJson.toString());
            }
        }
        return apiResponse;
    }

}

