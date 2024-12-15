package com.itt.assignment1.util;

import com.itt.assignment1.exception.CountryCodeNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CountryCodeMap {

    private final Map<String, String> countryCodeToCountryName;

    public CountryCodeMap() {
        countryCodeToCountryName = new HashMap<>();
        initialiseCountryCodeMapping();
    }

    private void initialiseCountryCodeMapping() {
        countryCodeToCountryName.put("IN", "India");
        countryCodeToCountryName.put("US", "United States of America");
        countryCodeToCountryName.put("CA", "Canada");
        countryCodeToCountryName.put("GB", "United Kingdom");
        countryCodeToCountryName.put("FR", "France");
        countryCodeToCountryName.put("DE", "Germany");
        countryCodeToCountryName.put("JP", "Japan");
        countryCodeToCountryName.put("CN", "China");
        countryCodeToCountryName.put("AU", "Australia");
        countryCodeToCountryName.put("BR", "Brazil");
        countryCodeToCountryName.put("ZA", "South Africa");
        countryCodeToCountryName.put("IT", "Italy");
        countryCodeToCountryName.put("ES", "Spain");
        countryCodeToCountryName.put("RU", "Russia");
        countryCodeToCountryName.put("MX", "Mexico");
        countryCodeToCountryName.put("KR", "South Korea");
        countryCodeToCountryName.put("AR", "Argentina");
        countryCodeToCountryName.put("SA", "Saudi Arabia");
        countryCodeToCountryName.put("AE", "United Arab Emirates");
        countryCodeToCountryName.put("SG", "Singapore");
        countryCodeToCountryName.put("SE", "Sweden");
        countryCodeToCountryName.put("CH", "Switzerland");
        countryCodeToCountryName.put("NL", "Netherlands");
        countryCodeToCountryName.put("NZ", "New Zealand");
        countryCodeToCountryName.put("BE", "Belgium");
        countryCodeToCountryName.put("PL", "Poland");
        countryCodeToCountryName.put("PT", "Portugal");
        countryCodeToCountryName.put("TR", "Turkey");
        countryCodeToCountryName.put("ID", "Indonesia");
    }

    public String getCountryName(String countryCode) throws CountryCodeNotFoundException {
        return Optional
                .ofNullable(countryCodeToCountryName.get(countryCode))
                .orElseThrow(() -> new CountryCodeNotFoundException(countryCode + " not found"));
    }

}
