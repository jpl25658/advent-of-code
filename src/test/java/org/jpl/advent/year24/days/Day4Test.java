package org.jpl.advent.year24.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day4Test {
    Day4 day = new Day4();

    @Test
    void testPart1() {
        assertEquals("18", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("9", day.setExample(1).part2().toString());
    }
}
