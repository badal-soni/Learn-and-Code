package com.itt.assignment1.validation;

import com.itt.assignment1.exception.InvalidInputException;

public class Validator {

    public static void validateCountryCode(String countryCode) throws InvalidInputException {
        if (countryCode.length() > 2) {
            throw new InvalidInputException("Invalid Country Code");
        }
        for (int index = 0; index < countryCode.length(); index++) {
            if (
                    Character.isDigit(countryCode.charAt(index)) ||
                    Character.isLowerCase(countryCode.charAt(index))
            ) {
                throw new InvalidInputException("Invalid Country Code");
            }
        }
    }

}
