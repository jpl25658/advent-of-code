package org.jpl.advent.year24.days;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jpl.advent.common.Pair;
import org.jpl.advent.year24.Day2024;

public class Day1 extends Day2024 {
  public Day1() {
    super(1);
  }

  public static void main(String[] args) {
    new Day1().printParts();
  }

  @Override
  public Object part1() {
    var input = parseInput();

    List<Integer> orderedLeft = input.getLeft().stream().sorted().toList();
    List<Integer> orderedRight = input.getRight().stream().sorted().toList();

    return IntStream.range(0, orderedLeft.size())
        .map(i -> Math.abs(orderedLeft.get(i) - orderedRight.get(i)))
        .sum();
  }

  @Override
  public Object part2() {
    var input = parseInput();

    Map<Integer, Long> ocurrences = input.getRight().stream()
        .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

    return input.getLeft().stream()
        .mapToLong(n -> n * ocurrences.getOrDefault(n, 0L))
        .sum();
  }

  private Pair<List<Integer>, List<Integer>> parseInput() {
    List<Integer> left = new ArrayList<>();
    List<Integer> right = new ArrayList<>();
    String[] nums = day().split("\\s+");
    for (var i = 0; i < nums.length; i += 2) {
      left.add(Integer.parseInt(nums[i]));
      right.add(Integer.parseInt(nums[i + 1]));
    }
    return new Pair<>(left, right);
  }

}
