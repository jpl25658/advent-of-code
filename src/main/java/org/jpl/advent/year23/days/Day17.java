package org.jpl.advent.year23.days;

import static java.lang.Character.getNumericValue;
import org.jpl.advent.year23.Day2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

public class Day17 extends Day2023 {
  public Day17() {
    super(17);
  }

  public static void main(String[] args) {
    new Day17().printParts();
  }

  @Override
  public Object part1() {
    Grid grid = readGrid();
    Coordinate start = new Coordinate(0, 0);
    Coordinate end = new Coordinate(grid.numRows - 1, grid.numCols - 1);

    return dijkstra(grid, start, end, 1);
  }

  @Override
  public Object part2() {
    Grid grid = readGrid();
    Coordinate start = new Coordinate(0, 0);
    Coordinate end = new Coordinate(grid.numRows - 1, grid.numCols - 1);

    return dijkstra(grid, start, end, 2);
  }

  private Grid readGrid() {
    var data = dayGrid();
    var numRows = data.length;
    var numCols = data[0].length;
    var values = new int[numRows][numCols];
    for (int r = 0; r < numRows; r++) {
      for (int c = 0; c < numCols; c++) {
        values[r][c] = getNumericValue(data[r][c]);
      }
    }
    return new Grid(values, numRows, numCols);
  }

  private static final Integer NORTH = 0;
  private static final Integer EAST = 1;
  private static final Integer SOUTH = 2;
  private static final Integer WEST = 3;

  private static final int LEFT = -1; // offset from direction to turn left (modulus 4)
  private static final int RIGHT = +1; // offset from direction to turn right (modulus 4)
  private static final int KEEP = 0; // offset from direction to keep moving

  private static final Map<Integer, Coordinate> MOVES =new HashMap<>();
  static {
    MOVES.put(NORTH, new Coordinate(-1, 0));
    MOVES.put(EAST, new Coordinate(0, 1));
    MOVES.put(SOUTH, new Coordinate(1, 0));
    MOVES.put(WEST, new Coordinate(0, -1));
  }

  private record Coordinate(int row, int col) implements Comparable<Coordinate> {
    public Coordinate add(int r, int c) {
      return new Coordinate(this.row + r, this.col + c);
    }
    public Coordinate add(Coordinate other) {
      return add(other.row(), other.col());
    }
    @Override
    public int compareTo(Coordinate other) {
      return this.row != other.row() ? Integer.compare(this.row, other.row()) : Integer.compare(this.col, other.col());
    }
  }

  private record Grid(int[][] v, int numRows, int numCols) {
    public boolean isNotValid(Coordinate c) {
      return c.row < 0 || c.row >= numRows || c.col < 0 || c.col >= numCols;
    }
    public int getV(Coordinate c) {
      return v[c.row][c.col];
    }
  }

  private record Node(Coordinate coord, int blocks, int direction) implements Comparable<Node> {
    @Override
    public int compareTo(Node other) {
      return this.direction == other.direction() && this.blocks != other.blocks()
          ? Integer.compare(this.blocks, other.blocks())
          : this.coord.compareTo(other.coord());
    }
  }

  private record State(Node node, int loss) implements Comparable<State> {
    @Override
    public int compareTo(State other) {
      return this.loss != other.loss() ? Integer.compare(this.loss, other.loss()) : this.node.compareTo(other.node());
    }
  }

  private int dijkstra(Grid grid, Coordinate start, Coordinate end, int part) {
    var queue = new PriorityQueue<State>();
    var visited = new HashSet<Node>();

    var eastStart = new Node(start.add(MOVES.get(EAST)), 1, EAST);
    var southStart = new Node(start.add(MOVES.get(SOUTH)), 1, SOUTH);

    queue.add(new State(eastStart, grid.getV(eastStart.coord)));
    queue.add(new State(southStart, grid.getV(southStart.coord)));

    while (! queue.isEmpty()) {
      var current = queue.poll();
      if (visited.contains(current.node)) {
        continue;
      }
      visited.add(current.node());
      if (end.equals(current.node.coord) && (part == 1 || current.node().blocks() >= 4)) {
        return current.loss();
      }
      queue.addAll(getAllPossibleMoves(grid, current, part));
    }
    return 0;
  }

  private List<State> getAllPossibleMoves(Grid grid, State state, int part) {
    return part == 1
        ? getAllPossibleMovesPart1(grid, state)
        : getAllPossibleMovesPart2(grid, state);
  }

  private List<State> getAllPossibleMovesPart1(Grid grid, State state) {
    var possibles = new ArrayList<State>();
    possibles.add(getPossibleMove(state, LEFT, grid, 1));
    possibles.add(getPossibleMove(state, RIGHT, grid, 1));
    if (state.node().blocks() < 3) {
      possibles.add(getPossibleMove(state, KEEP, grid, state.node().blocks() + 1));
    }
    return possibles.stream().filter(Objects::nonNull).toList();
  }

  private List<State> getAllPossibleMovesPart2(Grid grid, State state) {
    var possibles = new ArrayList<State>();
    if (state.node().blocks() >= 4) {
      possibles.add(getPossibleMove(state, LEFT, grid, 1));
      possibles.add(getPossibleMove(state, RIGHT, grid, 1));
    }
    if (state.node().blocks() < 10) {
      possibles.add(getPossibleMove(state, KEEP, grid, state.node().blocks() + 1));
    }
    return possibles.stream().filter(Objects::nonNull).toList();
  }

  private State getPossibleMove(State state, int offset, Grid grid, int newBlocks) {
    Node node = state.node();
    int newDirection = (node.direction() + offset + 4) % 4;  // modular
    Coordinate newCoord = node.coord().add(MOVES.get(newDirection));
    if (grid.isNotValid(newCoord)) {
      return null;
    }
    Node nextNode = new Node(newCoord, newBlocks, newDirection);
    int newLoss = state.loss() + grid.getV(newCoord);
    return new State(nextNode, newLoss);
  }

}
