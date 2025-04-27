package com.itt.assignment6;

import com.itt.assignment6.dto.Coordinates;
import com.itt.assignment6.service.GeoLocationService;

import java.util.List;

public class GeoLocationApplication {

    public static void main(String[] args) {
        GeoLocationService coordinatesService = new GeoLocationService();
        List<Coordinates> coordinates = coordinatesService.getCoordinates("India Gate");
        for (Coordinates coordinate: coordinates) {
            System.out.println("Coordinate: " + coordinate);
        }
    }

}
