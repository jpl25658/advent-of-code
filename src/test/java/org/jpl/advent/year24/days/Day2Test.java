package org.jpl.advent.year24.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day2Test {
    Day2 day = new Day2();

    @Test
    void testPart1() {
        assertEquals("2", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("4", day.setExample(1).part2().toString());
    }
}
