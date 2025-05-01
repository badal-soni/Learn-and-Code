package com.itt.assignment6.util;

import com.itt.assignment6.constant.ErrorMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertyReader {

    private static final String PROPERTIES_FILE = "application.properties";
    private final Properties properties;

    public PropertyReader() {
        this.properties = new Properties();
        this.init();
    }

    private void init() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (Objects.isNull(inputStream)) {
                throw new IOException(ErrorMessage.PROPERTY_NOT_FOUND + PROPERTIES_FILE);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.PROPERTY_NOT_FOUND + PROPERTIES_FILE, e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getApiKey(String key) {
        return getProperty(key);
    }

}
