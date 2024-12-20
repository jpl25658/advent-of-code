package org.jpl.advent.year24.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day15Test {
  Day15 day = new Day15();

  //@Test
  void testPart1() {
    assertEquals("10092", day.setExample(1).part1().toString());
  }

  //@Test
  void testPart1_2() {
    assertEquals("2028", day.setExample(2).part1().toString());
  }

  @Test
  void testPart2() {
    assertEquals("9021", day.setExample(1).part2().toString());
  }

  //@Test
  void testPart2_2() {
    assertEquals("618", day.setExample(6).part2().toString());
  }

}
