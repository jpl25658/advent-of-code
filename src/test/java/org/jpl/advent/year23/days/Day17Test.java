package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day17Test {
    Day17 day = new Day17();

    @Test
    void testPart1() {
        assertEquals("102", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("94", day.setExample(1).part2().toString());
        assertEquals("71", day.setExample(2).part2().toString());
    }
}
