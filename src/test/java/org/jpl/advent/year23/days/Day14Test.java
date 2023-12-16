package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day14Test {
    Day14 day = new Day14();

    @Test
    void testPart1() {
        assertEquals("136", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("64", day.setExample(1).part2().toString());
    }
}
