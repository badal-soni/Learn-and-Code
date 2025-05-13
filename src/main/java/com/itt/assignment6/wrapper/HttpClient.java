package com.itt.assignment6.wrapper;

import com.itt.assignment3.constant.HttpMethod;
import com.itt.assignment6.exception.ApiError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    public static String doGet(String requestUrl) {
        try {
            final HttpURLConnection connection = openConnection(requestUrl, HttpMethod.GET);
            return buildResponseBody(connection);
        } catch (Exception e) {
            throw new ApiError(e.getMessage());
        }
    }

    private static HttpURLConnection openConnection(
            String path,
            String httpMethod
    ) throws Exception {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(httpMethod);
        return connection;
    }

    private static String buildResponseBody(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseBuffer = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            responseBuffer.append(line);
        }

        reader.close();
        return responseBuffer.toString();
    }

}
