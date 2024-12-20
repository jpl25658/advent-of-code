package org.jpl.advent.year24.days;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.jpl.advent.common.Coord;
import org.jpl.advent.common.Grid;
import org.jpl.advent.year24.Day2024;

public class Day15 extends Day2024 {
  public Day15() {
    super(15);
  }

  public static void main(String[] args) {
    new org.jpl.advent.year24.days.Day15().printParts();
  }

  @Override
  public Object part1() {
    var input = parseInput();
    var grid = input.grid();
    var moves = input.moves();
    var robot = input.robot();

    for (var move : moves) {
      robot = tryMove(robot, move, grid);
    }

    var sum = 0;
    for (var r = 0; r < grid.rowLength(); r++) {
      for (var c = 0; c < grid.colLength(); c++) {
        if (grid.get(r, c) == BOX) {
          sum += (r * 100 + c);
        }
      }
    }
    return sum;
  }

  private Coord tryMove(Coord robot, Coord move, Grid grid) {
    char next = grid.get(robot.add(move));
    return switch (next) {
      case WALL -> robot;
      case EMPTY -> moveRobot(robot, move, grid);
      case BOX -> tryMoveBoxes(robot, move, grid) ? moveRobot(robot, move, grid) : robot;
      default -> throw new IllegalStateException("Unexpected value: " + next);
    };
  }

  private Coord moveRobot(Coord inicial, Coord move, Grid grid) {
    var robot = inicial.add(move);
    grid.set(robot, ROBOT);
    grid.set(inicial, EMPTY);
    return robot;
  }

  private boolean tryMoveBoxes(Coord robot, Coord move, Grid grid) {
    var actual = robot.add(move);
    while (grid.get(actual) == BOX) {
      actual = actual.add(move);
    }
    if (!(grid.get(actual) == EMPTY)) {
      return false;
    }
    grid.set(actual, BOX);
    grid.set(robot.add(move), EMPTY);
    return true;
  }

  @Override
  public Object part2() {
    var input = parseInput();

    return false;
  }

  private static final char EMPTY = '.';
  private static final char WALL = '#';
  private static final char BOX = 'O';
  private static final char ROBOT = '@';

  private record Input(Grid grid, List<Coord> moves, Coord robot) {
  }

  private Input parseInput() {
    Map<String, Coord> MOVES = Map.<String, Coord>of("^", Coord.N, "v", Coord.S, ">", Coord.E, "<", Coord.W);
    var parts = day().split("\n\n");
    var grid = new Grid(dayGrid(parts[0], DEFAULT_DELIMITER), EMPTY);
    var moves = Arrays.stream(parts[1].split("\n"))
        .flatMap(line -> Arrays.stream(line.split("")))
        .map(MOVES::get)
        .toList();
    return new Input(grid, moves, findRobot(grid));
  }

  private Coord findRobot(Grid grid) {
    for (var r = 0; r < grid.rowLength(); r++) {
      for (var c = 0; c < grid.colLength(); c++) {
        if (grid.get(r, c) == ROBOT) {
          return new Coord(r, c);
        }
      }
    }
    throw new IllegalStateException("Robot not found");
  }

}
