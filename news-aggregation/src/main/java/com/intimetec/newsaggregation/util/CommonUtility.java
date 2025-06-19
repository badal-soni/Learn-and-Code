package com.intimetec.newsaggregation.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public final class CommonUtility {

    private CommonUtility() {
    }

    public static LocalDate convertToLocalDate(final String date) {
        String timestamp = "2025-06-18T09:26:00Z";
        Instant instant = Instant.parse(timestamp);
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
