package com.intimetec.newsaggregation.client.view;

import com.intimetec.newsaggregation.client.constant.MenuChoices;
import com.intimetec.newsaggregation.client.constant.Messages;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.dto.request.UserSignInRequest;
import com.intimetec.newsaggregation.client.dto.request.UserSignUpRequest;
import com.intimetec.newsaggregation.client.dto.response.UserSignInResponse;
import com.intimetec.newsaggregation.client.factory.MenuFactory;
import com.intimetec.newsaggregation.client.service.AuthService;
import com.intimetec.newsaggregation.client.util.CommonUtility;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;

import java.util.Scanner;

public class WelcomeMenu {

    private final AuthService authService;
    private final ConsoleLogger consoleLogger;
    private final Scanner inputReader;

    public WelcomeMenu() {
        this.authService = new AuthService();
        this.consoleLogger = new ConsoleLogger();
        this.inputReader = new Scanner(System.in);
    }

    public boolean showWelcomeMenu() {
        for (String welcomeMenuChoice : MenuChoices.WELCOME_MENU_CHOICES) {
            consoleLogger.info(welcomeMenuChoice);
        }
        int choice = inputReader.nextInt();

        if (choice == 1) {
            signIn(inputReader);
        } else if (choice == 2) {
            signUp(inputReader);
        } else if (choice == 3) {
            UserContextHolder.accessToken = null;
            return false;
        }
        return true;
    }

    private void signIn(Scanner scanner) {
        UserSignInRequest signInRequest = new UserSignInRequest();

        consoleLogger.info(Messages.ENTER_EMAIL);
        signInRequest.setEmail(scanner.next());

        consoleLogger.info(Messages.ENTER_PASSWORD);
        signInRequest.setPassword(scanner.next());

        UserSignInResponse userSignInResponse = this.authService.signIn(signInRequest);
        if (userSignInResponse != null) {
            UserContextHolder.accessToken = userSignInResponse.getJwtToken();
            UserContextHolder.isLoggedIn = true;
            renderView(userSignInResponse);
        }
    }

    private void signUp(Scanner scanner) {
        UserSignUpRequest signUpRequest = new UserSignUpRequest();
        consoleLogger.info(Messages.ENTER_EMAIL);
        signUpRequest.setEmail(scanner.next());

        consoleLogger.info(Messages.ENTER_USERNAME);
        signUpRequest.setUsername(scanner.next());

        consoleLogger.info(Messages.ENTER_PASSWORD);
        signUpRequest.setPassword(scanner.next());
        this.authService.signUp(signUpRequest);
    }

    private void renderView(UserSignInResponse userSignInResponse) {
        while (UserContextHolder.accessToken != null) {
            UserContextHolder.accessToken = userSignInResponse.getJwtToken();
            MenuFactory
                    .getMenuPresenter(CommonUtility.getFirstRole(userSignInResponse.getRoles()))
                    .showMenu();
        }
    }

}
