package com.itt.assignment3.validation;

import com.itt.assignment3.constant.ErrorMessage;
import com.itt.assignment3.exception.RangeException;

public class Validator {

    public static void validateRange(String[] rangeArray) {
        if (rangeArray.length <= 1) {
            throw new RangeException(ErrorMessage.INVALID_PARAMETERS_ERROR_MESSAGE);
        }
        try {
            int start = Integer.parseInt(rangeArray[0]);
            int end = Integer.parseInt(rangeArray[1]);
            if ((end - start + 1) < 1) {
                throw new RangeException(ErrorMessage.RANGE_LENGTH_ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

}
