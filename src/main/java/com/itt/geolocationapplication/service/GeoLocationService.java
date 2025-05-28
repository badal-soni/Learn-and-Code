package com.itt.geolocationapplication.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.itt.geolocationapplication.constant.Constants;
import com.itt.geolocationapplication.constant.PropertyKeys;
import com.itt.geolocationapplication.dto.Coordinates;
import com.itt.geolocationapplication.util.Parser;
import com.itt.geolocationapplication.util.PropertyReader;
import com.itt.geolocationapplication.wrapper.HttpClient;

import java.util.List;

public class GeoLocationService {

    private final PropertyReader propertyReader;
    private final Parser responseParser;

    public GeoLocationService() {
        this.propertyReader = new PropertyReader();
        this.responseParser = new Parser();
    }

    public List<Coordinates> getCoordinates(String place) {
        String apiUrl = Constants.GEOCODE_SEARCH_URL + createSearchQuery(place);
        String responseBody = HttpClient.doGet(apiUrl);
        return this.responseParser.parse(
                responseBody,
                new TypeReference<List<Coordinates>>() {}
        );
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
        final String queryParam = "&api_key=";
        searchQuery.append(queryParam).append(getApiKey());
        return searchQuery.toString();
    }

    private String getApiKey() {
        return propertyReader.getApiKey(PropertyKeys.API_KEY);
    }

}
