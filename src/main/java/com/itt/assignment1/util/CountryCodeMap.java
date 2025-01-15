package com.itt.assignment1.util;

import com.google.gson.Gson;
import com.itt.assignment1.exception.CountryCodeNotFoundException;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CountryCodeMap {

    private static final String COUNTRY_CODE_JSON_FILE = "country-code.json";
    private final Map<String, List<String>> countryCodeToCountryName;

    public CountryCodeMap() {
        countryCodeToCountryName = new HashMap<>();
        initialiseCountryCodeMapping();
    }

    private void initialiseCountryCodeMapping() {
        try {
            Reader reader = new InputStreamReader(
                            getClass()
                            .getClassLoader()
                            .getResourceAsStream(COUNTRY_CODE_JSON_FILE)
            );

            Gson gson = new Gson();
            Map<String, List<String>> jsonMap = gson.fromJson(reader, Map.class);

            countryCodeToCountryName.putAll(jsonMap);

            reader.close();
        } catch (Exception e) {
            System.err.println("Error loading country codes from JSON file: " + e.getMessage());
        }
    }

    public List<String> getCountryName(String countryCode) throws CountryCodeNotFoundException {
        return Optional
                .ofNullable(countryCodeToCountryName.get(countryCode))
                .orElseThrow(() -> new CountryCodeNotFoundException(countryCode + " not found"));
    }

}
