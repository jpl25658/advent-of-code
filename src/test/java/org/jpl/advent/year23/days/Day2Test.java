package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day2Test {
    Day2 day = new Day2();

    @Test
    void testPart1() {
        assertEquals("8", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("2286", day.setExample(2).part2().toString());
    }
}
