package org.jpl.advent.year23.days;

import org.jpl.advent.year23.Day2023;

import java.util.ArrayList;
import java.util.List;

public class Day18 extends Day2023 {
  public Day18() {
    super(18);
  }

  public static void main(String[] args) {
    new Day18().printParts();
  }

  @Override
  public Object part1() {
    var movements =  dayStream().map(this::parseLine).toList();
    return calcTotalArea(movements);
  }

  @Override
  public Object part2() {
    var movements =  dayStream().map(this::parseLine2).toList();
    return calcTotalArea(movements);
  }

  private record Coordinate(long x, long y) {
    public Coordinate move(Coordinate other) {
      return new Coordinate(this.x + other.x(), this.y + other.y());
    }
  }
  private record Movement(String direction, long steps) {}

  private Movement parseLine(String line) {
    String[] parts = line.replaceAll("[\\(\\)]", "").split(" ");
    return new Movement(parts[0], Long.parseLong(parts[1]));
  }

  private Movement parseLine2(String line) {
    String part = line.replaceAll("[\\(\\)]", "").split("#")[1];
    String direction =part.substring(5);
    long steps = Long.parseLong(part.substring(0, 5), 16);
    return new Movement(direction, steps);
  }

  private long calcTotalArea(List<Movement> movements) {
    var vertices = calcVertices(movements);
    long trench = manhattan(vertices);
    long interior = shoelace(vertices) - (trench / 2L) + 1L;
    return trench + interior;
  }

  private List<Coordinate> calcVertices(List<Movement> movements) {
    var vertices = new ArrayList<Coordinate>();
    Coordinate actual = new Coordinate(0L, 0L);
    vertices.add(actual);
    for (var m : movements) {
      Coordinate offset = (switch (m.direction()) {
        case "R", "0" -> new Coordinate(m.steps(), 0);
        case "D", "1" -> new Coordinate(0, m.steps());
        case "L", "2" -> new Coordinate(-m.steps(), 0);
        case "U", "3" -> new Coordinate(0, -m.steps());
        default -> throw new IllegalStateException("Unexpected value: " + m.direction());
      });
      actual = actual.move(offset);
      vertices.add(actual);
    }
    vertices.add(new Coordinate(0L, 0L));
    return vertices;
  }

  private long manhattan(List<Coordinate> vertices) {
    long total = 0L;
    for (int i = 0; i < vertices.size() - 1; i++) {
      Coordinate c1 = vertices.get(i);
      Coordinate c2 = vertices.get(i + 1);
      total += (Math.abs(c1.x() - c2.x()) + Math.abs(c1.y() - c2.y()));
    }
    return total;
  }
  private long shoelace(List<Coordinate> vertices) {
    long total = 0;
    for (int i = 0; i < vertices.size() - 1; i++) {
      Coordinate c1 = vertices.get(i);
      Coordinate c2 = vertices.get(i + 1);
      total += (c1.x() * c2.y() - c1.y() * c2.x());
    }
    return Math.abs(total) / 2L;
  }


}
