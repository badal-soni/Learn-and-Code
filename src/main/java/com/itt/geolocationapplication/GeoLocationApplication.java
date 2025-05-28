package com.itt.geolocationapplication;

import com.itt.geolocationapplication.constant.Constants;
import com.itt.geolocationapplication.dto.Coordinates;
import com.itt.geolocationapplication.service.GeoLocationService;

import java.util.List;
import java.util.Scanner;

public class GeoLocationApplication {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(Constants.PLACE_INPUT_PROMPT);
        String place = scanner.nextLine();

        GeoLocationService coordinatesService = new GeoLocationService();
        List<Coordinates> coordinates = coordinatesService.getCoordinates(place);
        for (Coordinates coordinate : coordinates) {
            System.out.println(Constants.LOG_COORDINATE + coordinate);
        }
    }

}
