package org.jpl.advent.year24.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day7Test {
    Day7 day = new Day7();

    @Test
    void testPart1() {
        assertEquals("3749", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("11387", day.setExample(1).part2().toString());
    }
}
