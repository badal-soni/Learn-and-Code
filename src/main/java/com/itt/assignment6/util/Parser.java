package com.itt.assignment6.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.assignment6.constant.ErrorMessage;
import com.itt.assignment6.exception.ParsingException;

public class Parser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T parse(
            String json,
            TypeReference<T> classType
    ) {
        try {
            return objectMapper.readValue(json, classType);
        } catch (JsonProcessingException e) {
            throw new ParsingException(ErrorMessage.INVALID_FORMAT);
        }
    }

}
