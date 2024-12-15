package com.itt.assignment1;

import com.itt.assignment1.util.CountryCodeMap;
import com.itt.assignment1.validation.Validator;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final CountryCodeMap countryCodeMap = new CountryCodeMap();

        final String countryCode = readInput(scanner);

        try {
            Validator.validateCountryCode(countryCode);
            System.out.println(countryCodeMap.getCountryName(countryCode));
        } catch (Exception exception) {
            System.err.print(exception.getMessage());
        }
    }

    private static String readInput(Scanner scanner) {
        System.out.print("Enter country code: ");
        String countryCode = scanner.next();
        System.out.println();
        return countryCode;
    }

}


