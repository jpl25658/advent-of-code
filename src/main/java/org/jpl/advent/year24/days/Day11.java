package org.jpl.advent.year24.days;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jpl.advent.year24.Day2024;

public class Day11 extends Day2024 {
  public Day11() {
    super(11);
  }

  public static void main(String[] args) {
    new Day11().printParts();
  }

  @Override
  public Object part1() {
    var stones = parseInput();

    return stones.stream()
        .mapToLong(stone -> blinkStone(stone, 25))
        .sum();
  }

  @Override
  public Object part2() {
    var stones = parseInput();

    return stones.stream()
        .mapToLong(stone -> blinkStone(stone, 75))
        .sum();
  }

  private record StoneBlink(long stone, int blink) {
  }

  private long blinkStone(long stone, int blinks) {
    Map<StoneBlink, Long> memo = new HashMap<>();

    return blinkStoneMemoized(stone, blinks, memo);
  }

  private long blinkStoneMemoized(long stone, int blinks, Map<StoneBlink, Long> memo) {
    if (blinks == 0) {
      return 1L;
    }

    StoneBlink actual = new StoneBlink(stone, blinks);

    if (memo.containsKey(actual)) {
      return memo.get(actual);
    }

    long result;

    if (stone == 0) {
      result = blinkStoneMemoized(1L, blinks - 1, memo);
    } else {
      var str = Long.toString(stone);
      if (str.length() % 2 == 0) {
        var half = str.length() / 2;
        var left = Long.parseLong(str.substring(0, half));
        var right = Long.parseLong(str.substring(half));
        result = blinkStoneMemoized(left, blinks - 1, memo) + blinkStoneMemoized(right, blinks - 1, memo);
      } else {
        result = blinkStoneMemoized(stone * 2024L, blinks - 1, memo);
      }
    }
    memo.put(actual, result);
    return result;
  }

  private List<Long> parseInput() {
    return Arrays.stream(dayNumbers("\\s+")).boxed().toList();
  }

}
