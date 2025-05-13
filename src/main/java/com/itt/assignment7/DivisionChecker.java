package com.itt.assignment7;

public class DivisionChecker {

    public static int countNumbersWithEqualDivisors(int k) {
        int count = 0;
        for (int n = 2; n < k; n++) {
            count += countDivisors(n) == countDivisors(n + 1) ? 1 : 0;
        }
        return count;
    }

    private static int countDivisors(int n) {
        int count = 0;
        for (int i = 1; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                count += (i == n / i) ? 1 : 2;
            }
        }
        return count;
    }

}
