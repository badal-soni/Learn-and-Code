package com.itt.assignment7;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DivisionCheckerTest {

    @Test
    public void testCountNumbersWithEqualsDivisorsShouldPass() {
        final int[] input = {3, 15, 100};
        final int[] expectedOutput = {1, 2, 15};
        int index = 0;
        for (int k: input) {
            final int actualOutput = DivisionChecker.countNumbersWithEqualDivisors(k);
            Assert.assertEquals(expectedOutput[index++], actualOutput);
        }
    }

    @Test
    public void testCountNumbersWithEqualDivisorsShouldFail() {
        final int[] input = {3, 15, 100};
        final int[] expectedOutput = {1, 2, 14};
        int index = 0;
        List<Integer> failed = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            final int k = input[i];
            final int actualOutput = DivisionChecker.countNumbersWithEqualDivisors(k);
            if (actualOutput != expectedOutput[index]) {
                failed.add(index);
            } else if (i < input.length - 1) {
                Assert.assertEquals(expectedOutput[index++], actualOutput);
            }
        }
        Assert.assertFalse(failed.isEmpty());
    }

}
