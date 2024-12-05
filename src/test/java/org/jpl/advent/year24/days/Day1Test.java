package org.jpl.advent.year24.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day1Test {
    Day1 day = new Day1();

    @Test
    void testPart1() {
        assertEquals("11", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("31", day.setExample(1).part2().toString());
    }
}
