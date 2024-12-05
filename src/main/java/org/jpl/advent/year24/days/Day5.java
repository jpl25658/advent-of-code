package org.jpl.advent.year24.days;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jpl.advent.year24.Day2024;

public class Day5 extends Day2024 {
  public Day5() {
    super(5);
  }

  public static void main(String[] args) {
    new Day5().printParts();
  }

  @Override
  public Object part1() {
    var input = parseInput();

    return input.updates().stream()
        .filter(pages -> isValidUpdate(pages, input.rules()))
        .mapToInt(pages -> pages.get(pages.size()/2))
        .sum();
  }

  @Override
  public Object part2() {
    var input = parseInput();
    var comparator = new CompareByRules(input.rules());

    return input.updates().stream()
        .filter(pages -> !isValidUpdate(pages, input.rules()))
        .map(pages -> pages.stream().sorted(comparator).toList())
        .mapToInt(pages -> pages.get(pages.size()/2))
        .sum();
  }

  private record Rule(int before, int after) {}

  private record Input(List<List<Integer>> updates, List<Rule> rules) {}

  private Input parseInput() {
    var parts = day().split("\n\n");
    var rules = Arrays.stream(parts[0].split("\n")).map(this::getInputRule).toList();
    var updates = Arrays.stream(parts[1].split("\n")).map(this::getInputPages).toList();

    return new Input(updates, rules);
  }

  private Rule getInputRule(String ruleLine) {
    String[] values = ruleLine.split("\\|");

    return new Rule(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
  }

  private List<Integer> getInputPages(String pagesLine) {
    return Arrays.stream(pagesLine.split(",")).map(Integer::parseInt).toList();
  }

  private boolean isValidUpdate(List<Integer> pages, List<Rule> rules) {
    var positions = IntStream.range(0, pages.size()).boxed()
        .collect(Collectors.toMap(pages::get, i -> i));

    return rules.stream().noneMatch(rule -> isRuleBreak(rule, positions));
  }

  private boolean isRuleBreak(Rule rule, Map<Integer, Integer> positions) {
    return positions.containsKey(rule.before)
        && positions.containsKey(rule.after)
        && positions.get(rule.before) > positions.get(rule.after);
  }

  private record CompareByRules(List<Rule> rules) implements Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o2) {
      return rules.stream()
          .filter(rule -> (rule.before == o1 && rule.after == o2) || (rule.before == o2 && rule.after == o1))
          .map(rule -> rule.before == o1 ? -1 : 1)
          .findFirst()
          .orElse(0);
    }
  }

}
