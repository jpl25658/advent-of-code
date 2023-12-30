package org.jpl.advent.year23.days;


import org.jpl.advent.common.SlidingWindow;
import org.jpl.advent.year23.Day2023;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day21 extends Day2023 {
  public Day21() {
    super(21);
  }

  public static void main(String[] args) {
    new Day21().printParts();
  }

  @Override
  public Object part1() {
    int maxSteps = example == 0 ? 64 : 6;
    Grid grid = readGrid();
    Map<Coordinate, Integer> visited = dijkstra(grid, maxSteps);

    return visited.values().stream().filter(n -> n <= maxSteps && n % 2 == 0).count();
  }

  @Override
  public Object part2() {
    Grid grid = readGrid();
    // traverse for n in 0..3 steps: 131 * n + 65  (131 is the grid size, 65 if start position)
    // so, this moves up to the farthest edge of 0..3 grids from start
    var visited = IntStream.range(0, 4).boxed().parallel().map(n -> traverseGrid(grid, n)).sorted().toList();
    // var visited = List.of(3744L, 33417L, 92680L, 181533L);
    // how many additional increments for each additional grid ?
    var differences = SlidingWindow.of(visited, 2).map(list -> list.get(1) - list.get(0)).toList();
    // how difference grows for each additional grid ?
    var deltas = SlidingWindow.of(differences, 2).map(list -> list.get(1) - list.get(0)).toList();
    // grown is lineal ?
    assert(deltas.stream().allMatch(d -> d.equals(deltas.get(0))));
    // determine number of grids visited
    int gridSize = grid.numRows(); // 131 // or nCols, as grid is square
    int startToBorder = grid.start().row(); // 65 // or getCol() as grid is square and start centered
    int numSteps = 26_501_365; // 202300 * 131 + 65
    long nGrids = (numSteps - startToBorder) / gridSize; // 202300
    // result is the sum of first grid plus, for each additional grid traversed
    // the initial difference, incremented as many times as grid we are traversing
    return visited.get(0) + LongStream.range(0, nGrids).map(n -> differences.get(0) + deltas.get(0) * n).sum();
  }

  private static final char START = 'S';
  private static final char ROCK = '#';

  private static final Coordinate NORTH = new Coordinate(-1, 0);
  private static final Coordinate SOUTH = new Coordinate(1, 0);
  private static final Coordinate WEST = new Coordinate(0, -1);
  private static final Coordinate EAST = new Coordinate(0, 1);

  private Grid readGrid() {
    var data = dayGrid();
    var numRows = data.length;
    var numCols = data[0].length;
    var start = new AtomicReference<Coordinate>();
    var plots = IntStream.range(0, numRows).boxed()
        .flatMap(row ->  IntStream.range(0, numCols)
            .mapToObj(col -> new Coordinate(row, col)))
        .peek(coord -> { if (data[coord.row][coord.col] == START) start.set(coord); })
        .filter(coord -> data[coord.row][coord.col] != ROCK)
        .toList();
    return new Grid(plots, numRows, numCols, start.get());
  }

  private record Coordinate(int row, int col) implements Comparable<Coordinate> {
    public Coordinate add(Coordinate other) {
      return new Coordinate(row + other.row, col + other.col);
    }
    @Override
    public int compareTo(Coordinate other) {
      return this.row != other.row ? Integer.compare(this.row, other.row) : Integer.compare(this.col, other.col);
    }
  }

  private record Node(Coordinate coord, int steps) implements Comparable<Node> {
    @Override
    public int compareTo(Node other) {
      return steps != other.steps ? Integer.compare(steps, other.steps) : coord.compareTo(other.coord);
    }
  }

  private record Grid(List<Coordinate> plots, int numRows, int numCols, Coordinate start) {
    public boolean isPlotModulus(Coordinate c, int maxGrids) {
      Coordinate modulus = new Coordinate((c.row + numRows * maxGrids) % numRows, (c.col + numCols * maxGrids) % numCols);
      return plots.contains(modulus);
    }
    public boolean isPlot(Coordinate c) {
      return isPlotModulus(c, 1);
    }
  }

  private Map<Coordinate, Integer> dijkstra(Grid grid, int maxSteps) {
    var visited = new HashMap<Coordinate, Integer>();
    var active = new PriorityQueue<Node>();

    visited.put(grid.start, 0);
    active.add(new Node(grid.start, 0));

    while (! active.isEmpty()) {
      var actual = active.poll();
      if (actual.steps >= maxSteps) {
        continue;
      }
      var steps = visited.getOrDefault(actual.coord, Integer.MAX_VALUE);
      Stream.of(NORTH, SOUTH, EAST, WEST)
          .map(actual.coord::add).filter(grid::isPlot)
          .forEach(possible -> {
            var newSteps = steps + 1;
            if (newSteps < visited.getOrDefault(possible, Integer.MAX_VALUE)) {
              visited.put(possible, newSteps);
              active.add(new Node(possible, newSteps));
            }
          });
    }
    return visited;
  }

  private long traverseGrid(Grid grid, int maxGrids) {
    HashMap<Coordinate, Integer> visited = new HashMap<>();
    LinkedList<Node> pending = new LinkedList<>();
    int maxSteps = maxGrids * grid.numRows + grid.start.row;
    pending.add(new Node(grid.start, maxSteps));

    while (! pending.isEmpty()) {
      Node actual = pending.poll();
      Coordinate coord = actual.coord;
      int steps = actual.steps;
      if (steps >= visited.getOrDefault(coord, Integer.MAX_VALUE)) continue;
      visited.put(coord, steps);
      if (steps == 0) continue;
      List<Node> possibles = Stream.of(NORTH, SOUTH, EAST, WEST)
          .map(actual.coord::add)
          .filter(c -> grid.isPlotModulus(c, maxGrids))
          .map(c -> new Node(c, steps - 1))
          .toList();
      pending.addAll(possibles);
    }
    return visited.values().stream().filter(steps -> steps == 0).count();
  }

}
