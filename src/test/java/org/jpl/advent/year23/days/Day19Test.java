package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day19Test {
    Day19 day = new Day19();

    @Test
    void testPart1() {
        assertEquals("19114", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("167409079868000", day.setExample(1).part2().toString());
    }
}
