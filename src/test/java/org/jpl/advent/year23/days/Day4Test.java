package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day4Test {
    Day4 day = new Day4();

    @Test
    void testPart1() {
        assertEquals("13", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("30", day.setExample(2).part2().toString());
    }
}
