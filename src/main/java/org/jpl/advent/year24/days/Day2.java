package org.jpl.advent.year24.days;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import org.jpl.advent.year24.Day2024;

public class Day2 extends Day2024 {
  public Day2() {
    super(2);
  }

  public static void main(String[] args) {
    new Day2().printParts();
  }

  @Override
  public Object part1() {
      return parseInput().stream()
          .filter(this::isSafe)
          .count();
  }

  @Override
  public Object part2() {
    return parseInput().stream()
        .filter(report -> isSafe(report) || isSafeWhenRemoveOne(report))
        .count();
  }

  private List<int[]> parseInput() {
    return dayStream()
        .map(line -> line.split("\\s+"))
        .map(report -> Arrays.stream(report).mapToInt(Integer::parseInt).toArray())
        .toList();
  }

  private boolean isSafe(int[] report) {
    var diffs = IntStream.range(0, report.length - 1).map(i -> report[i + 1] - report[i]).toArray();
    // safe si todas son positivas o todas son negativas y ademas todas las diferencias están entre 1 y 3
    var allPositives = IntStream.of(diffs).allMatch(d -> d > 0);
    var allNegatives = IntStream.of(diffs).allMatch(d -> d < 0);
    var allInRange = IntStream.of(diffs).map(Math::abs).allMatch(d -> d >= 1 && d <= 3);;
    return (allPositives || allNegatives) && allInRange;
  }

  private boolean isSafeWhenRemoveOne(int[] report) {
    return IntStream.range(0, report.length)
        .anyMatch(ndx -> isSafe(removeOneFromIndex(report, ndx)));
  }

  private static int[] removeOneFromIndex(int[] report, int ndx) {
    return IntStream.range(0, report.length)
        .filter(i -> i != ndx)
        .map(i -> report[i])
        .toArray();
  }

}
