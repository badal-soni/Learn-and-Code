package com.intimetec.newsaggregation.client.view;

import com.intimetec.newsaggregation.client.constant.MenuChoices;
import com.intimetec.newsaggregation.client.constant.UserDashboardKeys;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.factory.MenuFactory;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;

import java.util.Scanner;

public class UserMenuPresenter implements MenuPresenter {

    private final Scanner scanner;
    private final ConsoleLogger consoleLogger;

    public UserMenuPresenter() {
        this.consoleLogger = new ConsoleLogger();
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void showMenu() {
        while (UserContextHolder.isLoggedIn) {
            if (this.renderOptions()) {
                return;
            }
        }
    }

    private boolean renderOptions() {
        for (String welcomeMenuChoice : MenuChoices.USER_MENU_CHOICES) {
            consoleLogger.info(welcomeMenuChoice);
        }
        int choice = scanner.nextInt();
        if (choice == 1) {
            MenuPresenter headlineMenu = MenuFactory.getMenuPresenter(UserDashboardKeys.HEADLINES);
            headlineMenu.showMenu();
        } else if (choice == 2) {
            MenuPresenter savedArticlesMenu = MenuFactory.getMenuPresenter(UserDashboardKeys.SAVED_ARTICLES);
            savedArticlesMenu.showMenu();
        } else if (choice == 3) {
            MenuPresenter searchNewsMenu = MenuFactory.getMenuPresenter(UserDashboardKeys.SEARCH);
            searchNewsMenu.showMenu();
        } else if (choice == 4) {
            MenuPresenter notificationMenu = MenuFactory.getMenuPresenter(UserDashboardKeys.NOTIFICATIONS);
            notificationMenu.showMenu();
        } else if (choice == 5) {
            UserContextHolder.clearContext();
            return false;
        }
        return true;
    }

}
