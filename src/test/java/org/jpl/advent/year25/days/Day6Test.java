package org.jpl.advent.year25.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day6Test {
    Day6 day = new Day6();

    @Test
    void testPart1() {
        assertEquals("4277556", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("3263827", day.setExample(1).part2().toString());
    }
}
