package org.jpl.advent.year24.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day5Test {
    Day5 day = new Day5();

    @Test
    void testPart1() {
        assertEquals("143", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
      assertEquals("123", day.setExample(1).part2().toString());
    }
}
