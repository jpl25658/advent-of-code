package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day1Test {
    Day1 day = new Day1();

    @Test
    void testPart1() {
        assertEquals("142", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("281", day.setExample(2).part2().toString());
    }
}
