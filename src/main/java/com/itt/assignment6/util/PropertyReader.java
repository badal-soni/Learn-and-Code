package com.itt.assignment6.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertyReader {

    private static final String PROPERTIES_FILE = "application.properties";
    private final Properties properties;

    public PropertyReader() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (Objects.isNull(inputStream)) {
                throw new IOException("Properties file not found in resources folder: " + PROPERTIES_FILE);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + PROPERTIES_FILE, e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getApiKey(String key) {
        return getProperty(key);
    }

}
