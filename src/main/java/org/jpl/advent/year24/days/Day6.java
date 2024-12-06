package org.jpl.advent.year24.days;

import java.util.HashSet;
import java.util.Set;
import org.jpl.advent.common.Coord;
import org.jpl.advent.common.Grid;
import org.jpl.advent.year24.Day2024;

public class Day6 extends Day2024 {

  public Day6() {
    super(6);
  }

  public static void main(String[] args) {
    new Day6().printParts();
  }

  @Override
  public Object part1() {
    var grid = parseInput();

    return walkGuard(grid).size();
  }

  @Override
  public Object part2() {
    var grid = parseInput();

    return walkGuard(grid).stream()
        .filter(position -> obstacleInPositionMakesGuardLoop(grid, position))
        .count();
  }

  public static final Coord[] DIRECTIONS = new Coord[] { Coord.N, Coord.E, Coord.S, Coord.W };

  public static final char OUT_GRID = '@';
  public static final char GUARD = '^';
  public static final char OBSTACLE = '#';

  private Grid parseInput() {
    return new Grid(dayGrid(), OUT_GRID);
  }

  private Set<Coord> walkGuard(Grid grid) {
    var visited = new HashSet<Coord>();

    var position = findGuardPosition(grid);
    var direction = 0;
    visited.add(position);
    while (grid.get(position) != OUT_GRID) {
      var nextPosition = position.add(DIRECTIONS[direction]);
      var content = grid.get(nextPosition);
      if (content == OBSTACLE) {
        direction = (direction + 1) % DIRECTIONS.length;
      } else {
        position = nextPosition;
        if (content != OUT_GRID) {
          visited.add(position);
        }
      }
    }
    return visited;
  }

  private Coord findGuardPosition(Grid grid) {
    for (var r = 0; r < grid.rowLength(); r++) {
      for (var c = 0; c < grid.colLength(); c++) {
        if (grid.get(r, c) == GUARD) {
          return new Coord(r, c);
        }
      }
    }
    System.out.println("error: guard not found");
    return new Coord(0,0);
  }

  private record PosAndDir(Coord position, int direction) {}

  private boolean obstacleInPositionMakesGuardLoop(Grid grid, Coord newObstacle) {
    if (grid.get(newObstacle) == GUARD) {
      return false;
    }

    var gridWithObstacle = grid.copy();
    gridWithObstacle.set(newObstacle, OBSTACLE);

    return walkGuardWithLoop(gridWithObstacle);
  }

  private boolean walkGuardWithLoop(Grid grid) {
    var visited = new HashSet<PosAndDir>();

    var position = findGuardPosition(grid);
    var direction = 0;
    visited.add(new PosAndDir(position, direction));
    var loopFound = false;
    while (grid.get(position) != OUT_GRID && !loopFound) {
      var nextPosition = position.add(DIRECTIONS[direction]);
      var content = grid.get(nextPosition);
      if (content == OBSTACLE) {
        direction = (direction + 1) % DIRECTIONS.length;
      } else {
        position = nextPosition;
        if (content != OUT_GRID) {
          var posAndDir = new PosAndDir(nextPosition, direction);
          if (visited.contains(posAndDir)) {
            loopFound = true;
          } else {
            visited.add(posAndDir);
          }
        }
      }
    }
    return loopFound;
  }
  
}
