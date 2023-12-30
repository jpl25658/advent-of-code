package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day21Test {
    Day21 day = new Day21();

    @Test
    void testPart1() {
        assertEquals("16", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("605492675373144", day.setExample(0).part2().toString());
    }
}
