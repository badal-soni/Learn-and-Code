package com.itt.assignment2.question1;

import java.util.Scanner;

public class RandomNumberGenerator {

    private static boolean isBetween1And100(int number) {
        return number >= 1 && number <= 100;
    }

    private static int getRandomNumber() {
        double randomNumber = Math.floor(Math.random() * 100) + 1;
        return (int) randomNumber;
    }

    private static int guessNumber(
            Scanner scanner,
            String inputPrompt
    ) {
        System.out.print(inputPrompt);
        int guess = scanner.nextInt();
        System.out.println();

        return guess;
    }

    private static void startGame(Scanner scanner) {
        double targetNumber = getRandomNumber();
        boolean isCorrectGuess = false;
        int guess = guessNumber(scanner, "Guess a number between 1 and 100:");
        int numberOfGuesses = 0;

        while (!isCorrectGuess) {
            if (!isBetween1And100(guess)) {
                guess = guessNumber(scanner, "I wont count this one, Please enter a number between 1 to 100");
                continue;
            } else {
                numberOfGuesses++;
            }

            if (guess < targetNumber) {
                guess = guessNumber(scanner, "Too low. Guess again");
            } else if (guess > targetNumber) {
                guess = guessNumber(scanner, "Too High. Guess again");
            } else {
                System.out.println("You guessed it in " + numberOfGuesses + " guesses!");
                isCorrectGuess = true;
            }
        }
    }

    public static void main(String[] args) {
        startGame(new Scanner(System.in));
    }

}
