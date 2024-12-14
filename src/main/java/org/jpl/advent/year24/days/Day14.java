package org.jpl.advent.year24.days;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.jpl.advent.common.Coord;
import org.jpl.advent.year24.Day2024;

public class Day14 extends Day2024 {
  public Day14() {
    super(14);
  }

  public static void main(String[] args) {
    new Day14().printParts();
  }

  @Override
  public Object part1() {
    var robots = parseInput();

    return robots.parallelStream()
        .map(this::moveRobot)
        .map(Robot::quadrant)
        .filter(q -> q >= 0)
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .values().stream()
        .reduce(1L, (a, b) -> a * b);
  }

  @Override
  public Object part2() {
    var floorSize = example == 0 ? new Coord(103, 101) : new Coord(7, 11);
    var robots = parseInput();

    var seconds = 0;
    while (true) {
      seconds++;
      robots = robots.parallelStream().map(Robot::moveInFloor).toList();
      Set<Coord> differentPositions = robots.parallelStream().map(Robot::pos).collect(Collectors.toSet());
      if (differentPositions.size() == robots.size()) {
        paintRobots(robots, floorSize, seconds);
        break;
      }
    }
    return seconds;
  }

  private record Robot(Coord pos, Coord vel, Coord size) {

    public Robot moveInFloor() {
      return new Robot(pos.add(vel).wrap(size), vel, size);
    }

    public int quadrant() {
      var colBorder = size.col() / 2;
      var rowBorder = size.row() / 2;
      if (pos.col() == colBorder || pos.row() == rowBorder) {
        return -1;
      }
      return (pos.col() < colBorder ? 0 : 1) + (pos.row() < rowBorder ? 0 : 2);
    }
  }

  private List<Robot> parseInput() {
    var floorSize = example == 0 ? new Coord(103, 101) : new Coord(7, 11);
    return dayStream()
        .map(line -> getRobot(line, floorSize))
        .toList();
  }

  private Robot getRobot(String line, Coord floorSize) {
    var matcher = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)").matcher(line);
    if (matcher.find()) {
      Coord pos = new Coord(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(1)));
      Coord vel = new Coord(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(3)));
      return new Robot(pos, vel, floorSize);
    }
    return null;
  }

  private Robot moveRobot(Robot robot) {
    var actual = robot;
    for (var i = 0; i < 100; i++) {
      actual = actual.moveInFloor();
    }
    return actual;
  }

  private void paintRobots(List<Robot> robots, Coord floorSize, int seconds) {
    var floor = new char[floorSize.row()][floorSize.row()];

    for (var r = 0; r < floorSize.row(); r++) {
      for (var c = 0; c < floorSize.col(); c++) {
        floor[r][c] = '.';
      }
    }
    robots.forEach(robot -> floor[robot.pos().row()][robot.pos().col()] = '*');

    System.out.printf("\nseconds: %d\n%n", seconds);
    for (var r = 0; r < floorSize.row(); r++) {
      for (var c = 0; c < floorSize.col(); c++) {
        System.out.print(floor[r][c]);
      }
      System.out.println("\n");
    }
    System.out.printf("\nseconds: %d\n%n", seconds);
  }
  
}
