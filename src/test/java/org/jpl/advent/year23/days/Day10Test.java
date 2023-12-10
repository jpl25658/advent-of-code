package org.jpl.advent.year23.days;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Day10Test {
  Day10 day = new Day10();

  @Test
  void testPart1a() {
    assertEquals("4", day.setExample(1).part1().toString());
  }

  @Test
  void testPart1b() {
    assertEquals("8", day.setExample(2).part1().toString());
  }

  @Test
  void testPart2a() {
    assertEquals("4", day.setExample(3).part2().toString());
  }

  @Test
  void testPart2b() {
    assertEquals("8", day.setExample(4).part2().toString());
  }

  @Test
  void testPart2c() {
    assertEquals("10", day.setExample(5).part2().toString());
  }

}
