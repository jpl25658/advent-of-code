package org.jpl.advent.year24.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day22Test {
    Day22 day = new Day22();

    @Test
    void testPart1() {
        assertEquals("37327623", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("23", day.setExample(2).part2().toString());
    }
}
