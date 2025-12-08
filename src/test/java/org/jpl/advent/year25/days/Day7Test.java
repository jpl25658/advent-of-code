package org.jpl.advent.year25.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day7Test {
    Day7 day = new Day7();

    //use to check code after implementation
    @Test
    void testPart1() {
        assertEquals("21", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("40", day.setExample(1).part2().toString());
    }
}
