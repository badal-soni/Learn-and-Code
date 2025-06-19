package com.intimetec.newsaggregation.constant;

public final class Constants {

    private static final String SPRING_ROLE_PREFIX = "ROLE_";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    public static final String SPRING_ROLE_ADMIN = SPRING_ROLE_PREFIX + ROLE_ADMIN;
    public static final String SPRING_ROLE_USER = SPRING_ROLE_PREFIX + ROLE_USER;
    public static final boolean SUCCESS_TRUE = true;
    public static final boolean SUCCESS_FALSE = false;
    public static final String LOWERCASE_REGULAR_EXPRESSION = "^[a-z]+$";
    public static final char API_KEY_SEPARATOR = '&';

    private Constants() {
    }

}
