package org.jpl.advent.year24.days;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jpl.advent.year24.Day2024;

public class Day13 extends Day2024 {
  public Day13() {
    super(13);
  }

  public static void main(String[] args) {
    new Day13().printParts();
  }

  @Override
  public Object part1() {
    var machines = parseInput();

    return machines.stream()
        .map(this::findSolution)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(point -> point.x.multiply(THREE).add(point.y))
        .reduce(BigDecimal::add)
        .get();
  }

  @Override
  public Object part2() {
    var machines = parseInput();

    return machines.stream()
        .map(m -> new Machine(m.a, m.b, m.p.withDelta()))
        .map(this::findSolution)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(point -> point.x.multiply(THREE).add(point.y))
        .reduce(BigDecimal::add)
        .get();
  }

  private static final BigDecimal DELTA = new BigDecimal("10000000000000");
  private static final BigDecimal THREE = new BigDecimal("3");

  private record Point(BigDecimal x, BigDecimal y) {
    public Point withDelta() {
      return new Point(x.add(DELTA), y.add(DELTA));
    }
  }

  private record Machine(Point a, Point b, Point p) {
  }

  private List<Machine> parseInput() {
    return dayStream("\n\n")
        .map(this::parseMachine)
        .toList();
  }

  private Machine parseMachine(String lines) {
    String regexButton = "X\\+(?<X>\\d+), Y\\+(?<Y>\\d+)";
    String regexPrize = "X=(?<X>\\d+), Y=(?<Y>\\d+)";
    var parts = lines.split("\n");
    var buttonA = parsePoint(regexButton, parts[0]);
    var buttonB = parsePoint(regexButton, parts[1]);
    var prize = parsePoint(regexPrize, parts[2]);
    return new Machine(buttonA, buttonB, prize);
  }

  private Point parsePoint(String regex, String line) {
    Matcher matcher = Pattern.compile(regex).matcher(line);
    if (matcher.find()) {
      return new Point(new BigDecimal(matcher.group("X")), new BigDecimal(matcher.group("Y")));
    }
    return null;
  }

  /*
  https://openstax.org/books/college-algebra-2e/pages/7-8-solving-systems-with-cramers-rule
  https://matrix.reshish.com/cramer.php

  x * Ax + x * Bx = Px
  y * Ay + y * By = Py
  denominator = Ax * By - Ay * Bx    denominator == 0 -> no solution
  x = Px * By - Py * Bx / denominator
  y = Py * Ax - Px * Ay / denominator
  */
  private Optional<Point> findSolution(Machine m) {
    var btnA = m.a();
    var btnB = m.b();
    var prize = m.p();
    var denominator = btnA.x.multiply(btnB.y).subtract(btnA.y.multiply(btnB.x));
    if (denominator.equals(BigDecimal.ZERO)) {
      return Optional.empty();
    }
    var pressA = (prize.x.multiply(btnB.y).subtract(prize.y.multiply(btnB.x))).divide(denominator, 0, RoundingMode.DOWN);
    var pressB = (prize.y.multiply(btnA.x).subtract(prize.x.multiply(btnA.y))).divide(denominator, 0, RoundingMode.DOWN);

    // validate
    if (!prize.x.equals(pressA.multiply(btnA.x).add(pressB.multiply(btnB.x)))
        || !prize.y.equals(pressA.multiply(btnA.y).add(pressB.multiply(btnB.y)))) {
      return Optional.empty();
    }

    return Optional.of(new Point(pressA, pressB));
  }

}
