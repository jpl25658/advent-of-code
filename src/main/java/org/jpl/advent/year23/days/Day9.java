package org.jpl.advent.year23.days;

import org.jpl.advent.year23.Day2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day9 extends Day2023 {
  public Day9() {
    super(9);
  }

  public static void main(String[] args) {
    new Day9().printParts();
  }

  @Override
  public Object part1() {
    return dayStream().map(Day9::listOfInteger)
        .mapToInt(Day9::extrapolate)
        .sum();
  }

  @Override
  public Object part2() {
    return dayStream().map(Day9::listOfInteger)
        .peek(Collections::reverse)
        .mapToInt(Day9::extrapolate)
        .sum();
  }

  private static List<Integer> listOfInteger(String input) {
    return Arrays.stream(input.split("\\s+")).map(Integer::valueOf).collect(Collectors.toList());
  }

  private static Integer extrapolate(List<Integer> list) {
    List<Integer> values = new ArrayList<>();
    LinkedList<Integer> actual = new LinkedList<>(list);
    while (actual.stream().anyMatch(n -> n != 0)) {
      values.add(actual.getLast());
      actual = new LinkedList<>(calcDifferences(actual));
    }
    return values.stream().mapToInt(Integer::intValue).sum();
  }

  private static LinkedList<Integer> calcDifferences(List<Integer> list) {
    return IntStream.range(1, list.size())
        .map(i -> list.get(i) - list.get(i - 1))
        .boxed()
        .collect(Collectors.toCollection(LinkedList::new));
  }

}
