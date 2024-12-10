package org.jpl.advent.year24.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day9Test {
    Day9 day = new Day9();

    @Test
    void testPart1() {
        assertEquals("1928", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("2858", day.setExample(1).part2().toString());
    }
}
