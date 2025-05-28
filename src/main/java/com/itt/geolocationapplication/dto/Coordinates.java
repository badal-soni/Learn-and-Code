package com.itt.geolocationapplication.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Coordinates {

    private String lat;
    private String lon;

    public void setLatitude(String lat) {
        this.lat = lat;
    }

    public void setLongitude(String lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "[Latitude: " + lat + ", Longitude: " + lon + "]";
    }

}
