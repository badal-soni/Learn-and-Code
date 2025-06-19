package com.intimetec.newsaggregation.client.view;

import com.intimetec.newsaggregation.client.dto.request.UserSignInRequest;
import com.intimetec.newsaggregation.client.dto.request.UserSignUpRequest;
import com.intimetec.newsaggregation.client.service.AuthService;
import com.intimetec.newsaggregation.client.util.Logger;

import java.util.Scanner;

public class WelcomeMenu {

    private final AuthService authService;
    private final Logger logger;

    public WelcomeMenu() {
        this.authService = new AuthService();
        this.logger = new Logger();
    }

    public boolean showWelcomeMenu() {
        logger.info("Welcome to the News Aggregator application. Please choose the options below.");
        logger.info("1. Login");
        logger.info("2. Sign up");
        logger.info("3. Exit");

        Scanner inputReader = new Scanner(System.in);
        int choice = inputReader.nextInt();

        System.out.println("Choice: " + choice);

        if (choice == 1) {
            signIn(inputReader);
        } else if (choice == 2) {
            signUp(inputReader);
        } else if (choice == 3) {
            // todo: signout
            return false;
        }
        return false;
    }

    private void signIn(Scanner scanner) {
        UserSignInRequest signInRequest = new UserSignInRequest();

        logger.info("Enter email: ");
        signInRequest.setEmail(scanner.next());

        logger.info("Enter password: ");
        signInRequest.setPassword(scanner.next());

        this.authService.signIn(signInRequest);
    }

    private void signUp(Scanner scanner) {
        UserSignUpRequest signUpRequest = new UserSignUpRequest();
        logger.info("Enter email: ");
        signUpRequest.setEmail(scanner.next());

        logger.info("Enter username: ");
        signUpRequest.setUsername(scanner.next());

        logger.info("Enter password:");
        signUpRequest.setPassword(scanner.next());
        this.authService.signUp(signUpRequest);
    }

}
