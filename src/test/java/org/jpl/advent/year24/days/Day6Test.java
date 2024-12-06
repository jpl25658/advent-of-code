package org.jpl.advent.year24.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day6Test {
    Day6 day = new Day6();

    @Test
    void testPart1() {
        assertEquals("41", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("6", day.setExample(1).part2().toString());
    }
}
