package org.jpl.advent.year24.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day12Test {
  Day12 day = new Day12();

  @Test
  void testPart1() {
    assertEquals("1930", day.setExample(4).part1().toString());
  }

  @Test
  void testPart2_1() {
    assertEquals("80", day.setExample(1).part2().toString());
  }

  @Test
  void testPart2_2() {
    assertEquals("436", day.setExample(3).part2().toString());
  }

  @Test
  void testPart2_3() {
    assertEquals("236", day.setExample(5).part2().toString());
  }

  @Test
  void testPart2_4() {
    assertEquals("368", day.setExample(6).part2().toString());
  }

  @Test
  void testPart2() {
    assertEquals("1206", day.setExample(4).part2().toString());
  }
}
