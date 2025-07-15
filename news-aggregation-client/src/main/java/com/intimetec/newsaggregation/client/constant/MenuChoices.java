package com.intimetec.newsaggregation.client.constant;

import java.util.List;

public class MenuChoices {

    public static final List<String> ADMIN_MENU_CHOICES = List.of(
            "1. Create a new external server\n",
            "2. View the list of external servers and status\n",
            "3. View the external server’s details\n",
            "4. Update/Edit the external server’s details\n",
            "5. Add new News Category\n",
            "6. View reported news\n",
            "7. Hide news\n",
            "8. Unhide news\n",
            "9. Hide category\n",
            "10. Unhide category\n",
            "11. View hidden news\n",
            "12. View hidden news categories\n",
            "13. Hide news by keyword\n",
            "14. Logout\n"
    );

    public static final List<String> WELCOME_MENU_CHOICES = List.of(
            "Welcome to the News Aggregator application. Please choose the options below.\n",
            "1. Login\n",
            "2. Sign up\n",
            "3. Exit\n"
    );

    public static final List<String> USER_MENU_CHOICES = List.of(
            "1. Headlines\n",
            "2. Saved Articles\n",
            "3. Search\n",
            "4. Notifications\n",
            "5. Logout\n"
    );

    public static final List<String> HEADLINES_MENU = List.of(
            "1. Today",
            "2. Date range",
            "3. Logout",
            "4. Back"
    );

    public static final List<String> SAVED_ARTICLES_MENU = List.of(
            "1. Unsave Article",
            "2. Back",
            "3. Logout"
    );

    public static final List<String> SEARCH_NEWS_MENU = List.of(
            "1. Search by text",
            "2. Logout",
            "3. Back"
    );

    public static final List<String> NOTIFICATION_MENU = List.of(
            "1. View Notifications",
            "2. Configure Notifications",
            "3. View keywords",
            "4. Add keyword",
            "5. Activate/Deactivate keyword",
            "6. Logout",
            "7. Back"
    );

}
