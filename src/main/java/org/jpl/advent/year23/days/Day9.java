package org.jpl.advent.year23.days;

import org.jpl.advent.year23.Day2023;

import java.util.ArrayList;
import java.util.Arrays;
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
    return dayStream().map(this::listOfInteger).mapToInt(this::extrapolateForward).sum();
  }

  @Override
  public Object part2() {
    return dayStream().map(this::listOfInteger).mapToInt(this::extrapolateBackward).sum();
  }

  private List<Integer> listOfInteger(String input) {
    return Arrays.stream(input.split("\\s+")).map(Integer::valueOf).toList();
  }

  private Integer extrapolateForward(List<Integer> list) {
    List<Integer> lastValues = new ArrayList<>();
    LinkedList<Integer> actual = new LinkedList<>(list);
    while (actual.stream().anyMatch(n -> n != 0)) {
      lastValues.add(actual.getLast());
      actual = new LinkedList<>(calcDifferences(actual));
    }
    return lastValues.stream().mapToInt(Integer::intValue).sum();
  }

  private LinkedList<Integer> calcDifferences(List<Integer> list) {
    return IntStream.range(1, list.size())
        .mapToObj(i -> list.get(i) - list.get(i - 1))
        .collect(Collectors.toCollection(LinkedList::new));
  }

  private Integer extrapolateBackward(List<Integer> list) {
    LinkedList<Integer> firstValues = new LinkedList<>();
    LinkedList<Integer> actual = new LinkedList<>(list);
    while (actual.stream().anyMatch(n -> n != 0)) {
      firstValues.push(actual.getFirst());
      actual = new LinkedList<>(calcDifferences(actual));
    }

    return firstValues.stream().reduce(0, (a, n) -> n - a);
  }

}
