package com.itt.assignment7;

import org.junit.Assert;
import org.junit.Test;

public class DivisionCheckerTest {

    @Test
    public void testCountNumbersWithEqualsDivisors() {
        final int[] input = {3, 15, 100};
        final int[] expectedOutput = {1, 2, 15};
        int index = 0;
        for (int k: input) {
            final int actualOutput = DivisionChecker.countNumbersWithEqualDivisors(k);
            Assert.assertEquals(expectedOutput[index++], actualOutput);
        }
    }

}
