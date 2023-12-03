package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day3Test {
    Day3 day = new Day3();

    @Test
    void testPart1() {
        assertEquals("4361", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("467835", day.setExample(2).part2().toString());
    }
}
