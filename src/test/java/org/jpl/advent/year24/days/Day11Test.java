package org.jpl.advent.year24.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day11Test {
    Day11 day = new Day11();

    @Test
    void testPart1() {
        assertEquals("55312", day.setExample(1).part1().toString());
    }

}
