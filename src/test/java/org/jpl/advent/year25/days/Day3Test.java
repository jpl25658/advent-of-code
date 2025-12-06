package org.jpl.advent.year25.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day3Test {
    Day3 day = new Day3();

    @Test
    void testPart1() {
        assertEquals("357", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("3121910778619", day.setExample(1).part2().toString());
    }
}
