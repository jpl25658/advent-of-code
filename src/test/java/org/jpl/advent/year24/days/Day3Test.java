package org.jpl.advent.year24.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day3Test {
    Day3 day = new Day3();

    @Test
    void testPart1() {
        assertEquals("161", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("48", day.setExample(2).part2().toString());
    }
}
