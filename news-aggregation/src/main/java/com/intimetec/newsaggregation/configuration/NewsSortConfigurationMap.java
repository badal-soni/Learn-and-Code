package com.intimetec.newsaggregation.configuration;

import java.util.HashMap;
import java.util.Map;

public class NewsSortConfigurationMap {

    private static final Map<String, String> sortByToDaoPropertyMap = new HashMap<>();

    static {
        sortByToDaoPropertyMap.put("likes", "userInteractions.interactionType");
        sortByToDaoPropertyMap.put("reports", "reportedNews");
        sortByToDaoPropertyMap.put("categories", "categories");
    }

    public static String getDaoProperty(String sortBy) {
        if (sortByToDaoPropertyMap.containsKey(sortBy)) {
            return sortByToDaoPropertyMap.get(sortBy);
        } else {
            throw new IllegalArgumentException("Invalid sort parameter: " + sortBy);
        }
    }

}
