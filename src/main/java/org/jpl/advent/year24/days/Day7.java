package org.jpl.advent.year24.days;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import org.jpl.advent.year24.Day2024;

public class Day7 extends Day2024 {
  public Day7() {
    super(7);
  }

  public static void main(String[] args) {
    new Day7().printParts();
  }

  @Override
  public Object part1() {
    var equations = parseInput();

    return equations.stream()
        .filter(equation -> testEquation(equation, false))
        .mapToLong(Equation::test)
        .sum();
  }

  @Override
  public Object part2() {
    var equations = parseInput();

    return equations.stream()
        .filter(equation -> testEquation(equation, true))
        .mapToLong(Equation::test)
        .sum();
  }

  private List<Equation> parseInput() {
    return dayStream().map(this::getEquation).toList();
  }

  private record Equation(long[] terms, long test) {
  }

  private Equation getEquation(String line) {
    var parts = line.split(":");
    var test = Long.parseLong(parts[0]);
    var terms = Arrays.stream(parts[1].trim().split("\\s")).mapToLong(Long::parseLong).toArray();
    return new Equation(terms, test);
  }

  private record State(long temp, int nextNdx) {
  }

  private boolean testEquation(Equation e, boolean useConcat) {
    Deque<State> states = new ArrayDeque<>();

    states.push(new State(e.terms[0], 1));
    while (!states.isEmpty()) {
      State s = states.pop();
      if (s.nextNdx() == e.terms().length && s.temp() == e.test()) {
        return true;
      }
      if (s.nextNdx() < e.terms().length) {
        var nextSum = s.temp() + e.terms()[s.nextNdx()];
        if (nextSum <= e.test()) {
          states.push(new State(nextSum, s.nextNdx() + 1));
        }
        var nextMul = s.temp() * e.terms()[s.nextNdx()];
        if (nextMul <= e.test()) {
          states.push(new State(nextMul, s.nextNdx() + 1));
        }
        if (useConcat) {
          var nextConcat = Long.parseLong("%d%d".formatted(s.temp(), e.terms()[s.nextNdx()]));
          if (nextConcat <= e.test()) {
            states.push(new State(nextConcat, s.nextNdx() + 1));
          }
        }
      }
    }

    return false;
  }

}
