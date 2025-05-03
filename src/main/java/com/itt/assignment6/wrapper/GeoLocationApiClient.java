package com.itt.assignment6.wrapper;

import com.itt.assignment3.constant.HttpMethod;
import com.itt.assignment6.constant.Constants;
import com.itt.assignment6.constant.PropertyKeys;
import com.itt.assignment6.dto.Coordinates;
import com.itt.assignment6.util.Parser;
import com.itt.assignment6.util.PropertyReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GeoLocationApiClient {

    private final PropertyReader propertyReader;
    private final Parser responseParser;

    public GeoLocationApiClient() {
        this.propertyReader = new PropertyReader();
        this.responseParser = new Parser();
    }

    public List<Coordinates> getCoordinates(String place) {
        try {
            String apiUrl = Constants.GEOCODE_SEARCH_URL + createSearchQuery(place);
            StringBuilder response = new StringBuilder();
            HttpURLConnection connection = buildConnection(apiUrl);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return responseParser.parse(response.toString());
        } catch (Exception e) {
            System.err.print(Constants.ERROR + e.getMessage());
        }
        return null;
    }

    private HttpURLConnection buildConnection(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(HttpMethod.GET);
        return connection;
    }

    private String createSearchQuery(String place) {
        String[] splittedByWhiteSpace = place.split(" ");
        StringBuilder searchQuery = new StringBuilder();
        for (int index = 0; index < splittedByWhiteSpace.length; index++) {
            searchQuery.append(splittedByWhiteSpace[index]);
            if (index < splittedByWhiteSpace.length - 1) {
                searchQuery.append('+');
            }
        }
        final String queryParam = "&api_key";
        searchQuery.append(queryParam).append(getApiKey());
        return searchQuery.toString();
    }

    private String getApiKey() {
        return propertyReader.getApiKey(PropertyKeys.API_KEY);
    }

}
