package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day9Test {
    Day9 day = new Day9();

    @Test
    void testPart1() {
        assertEquals("114", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("5 ", day.setExample(2).part2().toString());
    }
}
