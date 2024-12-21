package org.jpl.advent.year24.days;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.jpl.advent.year24.Day2024;

public class Day19 extends Day2024 {
  public Day19() {
    super(19);
  }

  public static void main(String[] args) {
    new Day19().printParts();
  }

  @Override
  public Object part1() {
    var input = parseInput();
    Map<String, Long> cache = new HashMap<>();

    return input.designs().stream()
        .filter(design -> matchDesign(design, input.towels(), cache) > 0)
        .count();
  }

  @Override
  public Object part2() {
    var input = parseInput();
    Map<String, Long> cache = new HashMap<>();

    return input.designs().stream()
        .mapToLong(design -> matchDesign(design, input.towels(), cache))
        .sum();
  }

  private record Input(List<String> designs, Set<String> towels) {}

  private Input parseInput() {
    var parts = day().split("\n\n");
    var towels = Arrays.stream(parts[0].split(", ")).collect(Collectors.toSet());
    var designs = Arrays.stream(parts[1].split("\n")).toList();

    return new Input(designs, towels);
  }

  private long matchDesign(String design, Set<String> towels, Map<String, Long> cache) {
    if (design.isEmpty()) return 1;
    if (cache.containsKey(design)) return cache.get(design);

    var result = towels.stream()
        .filter(design::startsWith)
        .mapToLong(towel -> matchDesign(design.substring(towel.length()), towels, cache))
        .sum();

    cache.put(design, result);
    return result;
  }

}
