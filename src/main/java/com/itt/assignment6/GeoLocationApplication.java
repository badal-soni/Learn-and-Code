package com.itt.assignment6;

import com.itt.assignment6.constant.Constants;
import com.itt.assignment6.dto.Coordinates;
import com.itt.assignment6.service.GeoLocationService;

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
