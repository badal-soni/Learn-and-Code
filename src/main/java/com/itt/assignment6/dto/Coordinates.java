package com.itt.assignment6.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Coordinates {

    private String lat;
    private String lon;

    public Coordinates() {

    }

    public Coordinates(
            String latitude,
            String longitude
    ) {
        this.lat = latitude;
        this.lon = longitude;
    }

    public String getLatitude() {
        return lat;
    }

    public String getLongitude() {
        return lon;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "[Latitude: " + lat + ", Longitude: " + lon + "]";
    }

}
