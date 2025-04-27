package com.itt.assignment6.service;

import com.itt.assignment6.dto.Coordinates;
import com.itt.assignment6.wrapper.GeoLocationApiClient;

import java.util.List;

public class GeoLocationService {

    private final GeoLocationApiClient apiClient;

    public GeoLocationService() {
        this.apiClient = new GeoLocationApiClient();
    }

    public List<Coordinates> getCoordinates(String place) {
        return this.apiClient.getCoordinates(place);
    }

}
