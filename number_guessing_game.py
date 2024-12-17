import random

def is_valid_guess(user_input):
    if user_input.isdigit() and 1 <= int(user_input) <= 100:
        return True
    else:
        return False

def main():
    target_number = random.randint(1, 100)
    guessed_correctly = False
    guess_attempts = 0
    
    user_guess = input("Guess a number between 1 and 100: ")

    while not guessed_correctly:
        if not is_valid_guess(user_guess):
            user_guess = input("Invalid input! Please enter a number between 1 and 100: ")
            continue
        else:
            guess_attempts += 1
            user_guess = int(user_guess)

        if user_guess < target_number:
            user_guess = input("Too low. Guess again: ")
        elif user_guess > target_number:
            user_guess = input("Too high. Guess again: ")
        else:
            print("You guessed it in", guess_attempts, "guesses!")
            guessed_correctly = True

main()
