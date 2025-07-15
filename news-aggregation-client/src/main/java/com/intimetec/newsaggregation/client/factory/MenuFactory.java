package com.intimetec.newsaggregation.client.factory;

import com.intimetec.newsaggregation.client.constant.Constants;
import com.intimetec.newsaggregation.client.constant.UserDashboardKeys;
import com.intimetec.newsaggregation.client.view.*;

public class MenuFactory {

    public static MenuPresenter getMenuPresenter(String menuType) {
        if (menuType.equals(Constants.ROLE_ADMIN)) {
            return new AdminMenuPresenter();
        } else if (menuType.equals(Constants.ROLE_USER)) {
            return new UserMenuPresenter();
        } else if (menuType.equals(UserDashboardKeys.HEADLINES)) {
            return new HeadlineMenuPresenter();
        } else if (menuType.equals(UserDashboardKeys.SAVED_ARTICLES)) {
            return new SavedArticleMenu();
        } else if (menuType.equals(UserDashboardKeys.SEARCH)) {
            return new SearchMenuPresenter();
        } else if (menuType.equals(UserDashboardKeys.NOTIFICATIONS)) {
            return new NotificationMenuPresenter();
        }
        throw new IllegalArgumentException("Invalid role: " + menuType);
    }

}
