package com.itt.assignment2.question2;

import java.util.Arrays;
import java.util.Scanner;

public class SubArrayExpectedValue {

    private static int[] getPrefixSumArray(int[] array) {
        int[] prefixSumArray = new int[array.length + 1];
        Arrays.fill(prefixSumArray, 0);

        for (int index = 1; index <= array.length; index++) {
            prefixSumArray[index] = prefixSumArray[index - 1] + array[index - 1];
        }
        return prefixSumArray;
    }

    private static int[] readArray(Scanner scanner, int size) {
        int[] array = new int[size];
        for (int index = 0; index < size; index++) {
            array[index] = scanner.nextInt();
        }
        return array;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int size = scanner.nextInt();
        int queries = scanner.nextInt();

        int[] array = readArray(scanner, size);
        int[] prefixSumArray = getPrefixSumArray(array);

        for (int query = 1; query <= queries; query++) {
            int leftIndex = scanner.nextInt();
            int rightIndex = scanner.nextInt();
            int subArraySum = prefixSumArray[rightIndex + 1] - prefixSumArray[leftIndex];
            int meanValue = subArraySum / (rightIndex - leftIndex + 1);
            System.out.println(meanValue);
        }
    }

}
