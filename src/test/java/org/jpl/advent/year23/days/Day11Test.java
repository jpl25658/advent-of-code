package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day11Test {
    Day11 day = new Day11();

    @Test
    void testPart1() {
        assertEquals("374", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2a() {
      day.setTestFactor(10);
      assertEquals("1030", day.setExample(1).part2().toString());
    }
    @Test
    void testPart2b() {
      day.setTestFactor(100);
      assertEquals("8410", day.setExample(1).part2().toString());
    }
}
