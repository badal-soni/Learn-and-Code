package com.intimetec.newsaggregation.util;

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

}
