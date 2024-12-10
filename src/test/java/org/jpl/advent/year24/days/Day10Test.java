package org.jpl.advent.year24.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day10Test {
    Day10 day = new Day10();

    @Test
    void testPart1() {
        assertEquals("36", day.setExample(5).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("81", day.setExample(5).part2().toString());
    }
}
