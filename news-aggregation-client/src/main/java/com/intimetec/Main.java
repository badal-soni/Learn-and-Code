package com.intimetec;

import com.intimetec.newsaggregation.client.view.WelcomeMenu;
import com.intimetec.newsaggregation.client.util.ConsoleLogger;

public class Main {

    public static void main(String[] args) {
        ConsoleLogger consoleLogger = new ConsoleLogger();
        run(consoleLogger);
    }

    private static void run(ConsoleLogger consoleLogger) {
        while (true) {
            WelcomeMenu welcomeMenu = new WelcomeMenu();
            welcomeMenu.showWelcomeMenu();
        }
    }
}