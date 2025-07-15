package com.intimetec.newsaggregation.client.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.intimetec.newsaggregation.client.constant.HttpHeader;
import com.intimetec.newsaggregation.client.constant.HttpMethod;
import com.intimetec.newsaggregation.client.constant.MediaType;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;
import com.intimetec.newsaggregation.client.logger.FileLogger;
import com.intimetec.newsaggregation.dto.response.ApiErrorResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpClient {

    private final ObjectMapper objectMapper;
    private final FileLogger fileLogger;
    private final ConsoleLogger consoleLogger;

    public HttpClient() {
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.fileLogger = FileLogger.getInstance();
        this.consoleLogger = new ConsoleLogger();
    }

    public <T> ApiResponse<T> get(String urlString, Map<String, String> headers, Class<T> responseType) throws Exception {
        return executeRequest(HttpMethod.GET, urlString, null, headers, responseType);
    }

    public <T> ApiResponse<List<T>> getList(String urlString, Map<String, String> headers, Class<T> responseType) throws Exception {
        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, responseType);
        return executeRequest(HttpMethod.GET, urlString, null, headers, listType);
    }

    public <T, R> ApiResponse<T> post(String urlString, R requestBody, Map<String, String> headers, Class<T> responseType) throws Exception {
        return executeRequest(HttpMethod.POST, urlString, requestBody, headers, responseType);
    }

    public <T, R> ApiResponse<List<T>> postList(String urlString, R requestBody, Map<String, String> headers, Class<T> responseType) throws Exception {
        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, responseType);
        return executeRequest(HttpMethod.POST, urlString, requestBody, headers, listType);
    }

    public <T, R> ApiResponse<T> put(String urlString, R requestBody, Map<String, String> headers, Class<T> responseType) throws Exception {
        return executeRequest(HttpMethod.PUT, urlString, requestBody, headers, responseType);
    }

    public ApiResponse<Void> delete(String urlString, Map<String, String> headers, String requestBody) throws Exception {
        return executeRequest(HttpMethod.DELETE, urlString, requestBody, headers, Void.class);
    }

    private <T, R> ApiResponse<T> executeRequest(String method, String urlString, R requestBody, Map<String, String> headers, Class<T> responseType) throws Exception {
        return executeRequest(method, urlString, requestBody, headers, (JavaType) objectMapper.getTypeFactory().constructType(responseType));
    }

    private <T, R> ApiResponse<T> executeRequest(String method, String urlString, R requestBody, Map<String, String> headers, JavaType responseType) throws Exception {
        HttpURLConnection connection = setupConnection(urlString, method, headers);

        if (requestBody != null) {
            writeRequestBody(connection, requestBody);
        }

        int responseCode = connection.getResponseCode();
        fileLogger.info(HttpClient.class + ": URL: " + urlString + " response status: " + responseCode);
        return processResponse(connection, responseType);
    }

    private HttpURLConnection setupConnection(String urlString, String method, Map<String, String> headers) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(!method.equals(HttpMethod.GET));
        connection.setRequestProperty(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        if (Objects.nonNull(headers)) {
            headers.forEach(connection::setRequestProperty);
        }
        return connection;
    }

    private <R> void writeRequestBody(HttpURLConnection connection, R requestBody) throws IOException {
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonRequestBody.getBytes());
            os.flush();
        }
    }

    private <T> ApiResponse<T> processResponse(HttpURLConnection connection, JavaType responseType) throws Exception {
        int responseCode = connection.getResponseCode();
        String httpStatus = connection.getResponseMessage();
        boolean success = (responseCode >= 200 && responseCode < 300);

        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setHttpStatus(httpStatus);
        apiResponse.setSuccess(success);

        if (success) {
            String responseJson = readInputStream(connection.getInputStream());
            if (!responseType.getRawClass().equals(Void.class)) {
                JavaType apiResponseType = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, responseType);
                ApiResponse<T> fullResponse = objectMapper.readValue(responseJson, apiResponseType);
                apiResponse.setData(fullResponse.getData());
            }
            apiResponse.setMessage("Request was successful");
        } else {
            ApiErrorResponse errorResponse = objectMapper.readValue(readInputStream(connection.getErrorStream()), ApiErrorResponse.class);
            consoleLogger.info(errorResponse.getMessage());
            apiResponse.setMessage(errorResponse.getMessage());
        }

        return apiResponse;
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        if (Objects.isNull(inputStream)) {
            return "";
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
}

