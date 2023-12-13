package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day13Test {
    Day13 day = new Day13();

    @Test
    void testPart1() {
        assertEquals("405", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("400", day.setExample(1).part2().toString());
    }
}
