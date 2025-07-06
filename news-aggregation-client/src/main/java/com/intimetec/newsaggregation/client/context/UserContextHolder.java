package com.intimetec.newsaggregation.client.context;

public class UserContextHolder {

    public static boolean isLoggedIn = false;
    public static String userRole = null;
    public static String accessToken = null;

    public static void clearContext() {
        isLoggedIn = false;
        userRole = null;
        accessToken = null;
    }

}
