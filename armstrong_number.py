def calculate_armstrong_sum(number):
    armstrong_sum = 0
    num_of_digits = 0

    temp_number = number
    while temp_number > 0:
        num_of_digits += 1
        temp_number //= 10

    temp_number = number
    while temp_number > 0:
        last_digit = temp_number % 10
        armstrong_sum += last_digit ** num_of_digits
        temp_number //= 10

    return armstrong_sum

input_number = int(input("\nPlease Enter a Number to Check if it is an Armstrong Number: "))

if input_number == calculate_armstrong_sum(input_number):
    print("\n %d is Armstrong Number.\n" % input_number)
else:
    print("\n %d is Not a Armstrong Number.\n" % input_number)
