import random

def roll_dice(sides):
    result = random.randint(1, sides)
    return result

def main():
    dice_sides = 6
    is_rolling = True
    
    while is_rolling:
        user_input = input("Ready to roll the dice? Enter 'Q' to Quit: ")
        if user_input.lower() != "q":
            rolled_number = roll_dice(dice_sides)
            print("You rolled a", rolled_number)
        else:
            is_rolling = False
            print("Thanks for playing!")
