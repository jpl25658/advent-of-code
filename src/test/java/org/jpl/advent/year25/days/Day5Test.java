package org.jpl.advent.year25.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day5Test {
    Day5 day = new Day5();

    @Test
    void testPart1() {
        assertEquals("3", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("14", day.setExample(1).part2().toString());
    }
}
