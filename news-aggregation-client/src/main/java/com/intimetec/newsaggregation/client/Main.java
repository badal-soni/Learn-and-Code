package com.intimetec.newsaggregation.client;

import com.intimetec.newsaggregation.client.logger.FileLogger;
import com.intimetec.newsaggregation.client.view.WelcomeMenu;

public class Main {

    public static void main(String[] args) {
        FileLogger fileLogger = FileLogger.getInstance();
        while (true) {
            run(fileLogger);
        }
    }

    private static void run(FileLogger fileLogger) {
        try {
            WelcomeMenu welcomeMenu = new WelcomeMenu();
            welcomeMenu.showWelcomeMenu();
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            fileLogger.error(Main.class + ": " + exception.getMessage());
        }
    }
}