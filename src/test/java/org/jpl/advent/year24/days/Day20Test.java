package org.jpl.advent.year24.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day20Test {
    Day20 day = new Day20();

    @Test
    void testPart1() {
      var expected = """
        There are 14 cheats that save 2 picoseconds.
        There are 14 cheats that save 4 picoseconds.
        There are 2 cheats that save 6 picoseconds.
        There are 4 cheats that save 8 picoseconds.
        There are 2 cheats that save 10 picoseconds.
        There are 3 cheats that save 12 picoseconds.
        There is one cheat that saves 20 picoseconds.
        There is one cheat that saves 36 picoseconds.
        There is one cheat that saves 38 picoseconds.
        There is one cheat that saves 40 picoseconds.
        There is one cheat that saves 64 picoseconds.
        
        At least 100 saving: 0""";

      assertEquals(expected, (day.setExample(1)).part1().toString());
    }

    @Test
    void testPart2() {
        assertEquals("true", day.setExample(1).part2().toString());
    }
}
