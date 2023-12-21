package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day18Test {
    Day18 day = new Day18();

    @Test
    void testPart1() {
        assertEquals("62", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("952408144115", day.setExample(1).part2().toString());
    }
}
