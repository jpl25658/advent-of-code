package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day12Test {
    Day12 day = new Day12();

    @Test
    void testPart1() {
        assertEquals("21", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("525152", day.setExample(1).part2().toString());
    }
}
