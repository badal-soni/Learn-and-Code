package com.intimetec.newsaggregation.client.constant;

public final class ApiUrls {

    private static final String BASE_URL = "http://localhost:2020/api/v1";
    public static final String SIGN_UP = BASE_URL + "/auth/sign-up";
    public static final String SIGN_IN = BASE_URL + "/auth/sign-in";
    public static final String EXTERNAL_SERVER = BASE_URL + "/server-configurations";
    public static final String CATEGORIES = BASE_URL + "/categories";
    public static final String NEWS = BASE_URL + "/news";
    public static final String NOTIFICATIONS = BASE_URL + "/notifications";
    public static final String TODAY_NEWS = BASE_URL + "/news/headlines/today";
    public static final String HEADLINES = BASE_URL + "/news/headlines";
    public static final String SEARCH_NEWS = BASE_URL + "/news/search";
    public static final String NOTIFICATION_CONFIGURATIONS = BASE_URL + "/notifications-configurations";
    public static final String REPORT_NEWS = BASE_URL + "/%s/report";
    public static final String REPORTED_NEWS = BASE_URL + "/news/reported";
    public static final String HIDE_NEWS = BASE_URL + "/news/hide";
    public static final String UNHIDE_NEWS = BASE_URL + "/news/unhide";
    public static final String HIDDEN_NEWS = BASE_URL + "/news/hidden";
    public static final String HIDE_CATEGORY = BASE_URL + "/categories/hide";
    public static final String HIDDEN_CATEGORIES = BASE_URL + "/categories/hidden";
    public static final String SAVED_NEWS = BASE_URL + "/dashboard/news/saved";
    public static final String KEYWORDS = BASE_URL + "/keywords";
    public static final String TOGGLE_KEYWORD_ACTIVE_STATUS = BASE_URL + "/%s/status/toggle";
    public static final String NEWS_ID = BASE_URL + "/%s";

}
