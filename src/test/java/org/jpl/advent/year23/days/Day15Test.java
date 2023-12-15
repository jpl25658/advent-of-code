package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day15Test {
    Day15 day = new Day15();

    @Test
    void testPart1() {
        assertEquals("1320", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("145", day.setExample(1).part2().toString());
    }
}
