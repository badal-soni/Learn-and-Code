package com.intimetec.newsaggregation.util;

import com.intimetec.newsaggregation.entity.News;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public final class CommonUtility {

    private CommonUtility() {
    }

    public static LocalDate convertToLocalDate(final String date) {
        String timestamp = "2025-06-18T09:26:00Z";
        Instant instant = Instant.parse(timestamp);
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String buildRegularExpressionForKeywordMatching(List<String> keywords) {
        final char regexpKeyword = '|';
        StringBuilder pattern = new StringBuilder();
        for (int index = 0; index < keywords.size(); index++) {
            pattern.append(keywords.get(index));
            if (index < keywords.size() - 1) {
                pattern.append(regexpKeyword);
            }
        }
        return pattern.toString();
    }

    public static boolean isRelationalField(String fieldName) {
        try {
            final Field field = News.class.getDeclaredField(fieldName);
            final Class<?> fieldType = field.getType();
            return !fieldType.isPrimitive() && !isWrapper(fieldType) && !fieldType.isAssignableFrom(String.class);
        } catch (NoSuchFieldException exception) {
            return false;
        }
    }

    public static boolean isEntityField(String fieldName) {
        try {
            News.class.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException exception) {
            return false;
        }
    }

    private static boolean isWrapper(Class<?> field) {
        return field.isAssignableFrom(Integer.class) || field.isAssignableFrom(Double.class) || field.isAssignableFrom(Long.class) || field.isAssignableFrom(Float.class);
    }

}
