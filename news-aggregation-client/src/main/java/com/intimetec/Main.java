package com.intimetec;

import com.intimetec.newsaggregation.client.view.WelcomeMenu;
import com.intimetec.newsaggregation.client.util.Logger;

public class Main {

    public static void main(String[] args) {
        Logger logger = new Logger();
        run(logger);
    }

    private static void run(Logger logger) {
        while (true) {
            WelcomeMenu welcomeMenu = new WelcomeMenu();
            boolean isApplicationExited = welcomeMenu.showWelcomeMenu();
            if (isApplicationExited) {
                logger.info("Shutting down the client application...");
                break;
            }
        }
    }
}