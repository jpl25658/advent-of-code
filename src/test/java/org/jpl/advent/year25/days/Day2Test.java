package org.jpl.advent.year25.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day2Test {
    Day2 day = new Day2();

    @Test
    void testPart1() {
        assertEquals("1227775554", day.setExample(1).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("4174379265", day.setExample(1).part2().toString());
    }
}
