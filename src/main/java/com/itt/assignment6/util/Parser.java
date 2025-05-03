package com.itt.assignment6.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.assignment6.dto.Coordinates;
import com.itt.assignment6.exception.ApiError;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    public List<Coordinates> parse(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Coordinates> coordinates = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);

            for (JsonNode node : rootNode) {
                String latitude = node.get("lat").asText();
                String longitude = node.get("lon").asText();
                coordinates.add(new Coordinates(latitude, longitude));
            }
        } catch (IOException e) {
            throw new ApiError(e.getMessage());
        }
        return coordinates;
    }

}
