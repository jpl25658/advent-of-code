package org.jpl.advent.year25.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day8Test {
    Day8 day = new Day8();

    @Test
    void testPart1() {
        assertEquals("40", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("25272", day.setExample(1).part2().toString());
    }
}
