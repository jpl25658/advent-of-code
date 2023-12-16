package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day16Test {
    Day16 day = new Day16();

    @Test
    void testPart1() {
        assertEquals("46", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("51", day.setExample(1).part2().toString());
    }
}
