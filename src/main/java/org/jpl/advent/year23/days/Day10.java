package org.jpl.advent.year23.days;

import static java.util.function.Predicate.not;
import lombok.Getter;
import lombok.Setter;
import org.jpl.advent.year23.Day2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;

public class Day10 extends Day2023 {
  public Day10() {
    super(10);
  }

  public static void main(String[] args) {
    new Day10().printParts();
  }

  private record Coordinate(int row, int col) {
    public static final Coordinate GO_NORTH = new Coordinate(-1, 0);
    public static final Coordinate GO_SOUTH = new Coordinate(1, 0);
    public static final Coordinate GO_EAST = new Coordinate(0, 1);
    public static final Coordinate GO_WEST = new Coordinate(0, -1);

    public Coordinate add(Coordinate other) {
      return new Coordinate(row + other.row, col + other.col);
    }
  }

  @Getter
  enum PipeType {
    GROUND('.', List.of()),
    NORTH_SOUTH('|', List.of(Coordinate.GO_NORTH, Coordinate.GO_SOUTH)),
    EAST_WEST('-', List.of(Coordinate.GO_EAST, Coordinate.GO_WEST)),
    NORTH_EAST('L', List.of(Coordinate.GO_NORTH, Coordinate.GO_EAST)),
    NORTH_WEST('J', List.of(Coordinate.GO_NORTH, Coordinate.GO_WEST)),
    SOUTH_EAST('F', List.of(Coordinate.GO_SOUTH, Coordinate.GO_EAST)),
    SOUTH_WEST('7', List.of(Coordinate.GO_SOUTH, Coordinate.GO_WEST)),
    START('S', List.of());

    private final char symbol;
    private final List<Coordinate> movements;

    PipeType(char symbol, List<Coordinate> movements) {
      this.symbol = symbol;
      this.movements = movements;
    }

    public static PipeType lookup(char c) {
      return Arrays.stream(values()).filter(p -> p.symbol == c).findFirst()
          .orElseThrow(() -> new IllegalStateException("Unexpected PipeType symbol: " + c));
    }
  }

  @Getter
  @Setter
  private static class Pipe {
    private final Coordinate coord;
    private PipeType type;
    private List<Coordinate> allowed;
    private boolean visited;
    private int distance = 0;
    public Pipe(int row, int col, char type) {
      this.coord = new Coordinate(row, col);
      this.type = PipeType.lookup(type);
      this.allowed = this.type.movements;
    }
  }

  @Getter
  private static class PipeGrid {
    private final Pipe[][] grid;
    private final int nRows;
    private final int nCols;
    Pipe start;

    PipeGrid(char[][] gridChar) {
      nRows = gridChar.length;
      nCols = gridChar[0].length;
      grid = new Pipe[nRows][nCols];
      initializeGrid(gridChar);
    }

    private void initializeGrid(char[][] gridChar) {
      for (int row = 0; row < nRows; row++) {
        for (int col = 0; col < nCols; col++) {
          Pipe pipe = new Pipe(row, col, gridChar[row][col]);
          grid[row][col] = pipe;
          if (pipe.getType() == PipeType.START)
            start = pipe;
        }
      }
      initializeStartMovements();
      adjustStartType();
    }

    private void initializeStartMovements() {
      if (start != null) {
        List<Coordinate> movements = new ArrayList<>();
        check(start, Coordinate.GO_NORTH, List.of(PipeType.NORTH_SOUTH, PipeType.SOUTH_EAST, PipeType.SOUTH_WEST), movements);
        check(start, Coordinate.GO_SOUTH, List.of(PipeType.NORTH_SOUTH, PipeType.NORTH_EAST, PipeType.NORTH_WEST), movements);
        check(start, Coordinate.GO_EAST, List.of(PipeType.EAST_WEST, PipeType.NORTH_WEST, PipeType.SOUTH_WEST), movements);
        check(start, Coordinate.GO_WEST, List.of(PipeType.EAST_WEST, PipeType.NORTH_EAST, PipeType.SOUTH_EAST), movements);
        start.setAllowed(movements);
      }
    }

    private void check(Pipe pipe, Coordinate go, List<PipeType> allowed, List<Coordinate> movements) {
      getPipe(pipe.coord.add(go))
          .filter(p -> allowed.contains(p.getType()))
          .ifPresent(ignored -> movements.add(go));
    }

    public Optional<Pipe> getPipe(Coordinate coord) {
      return isValid(coord) ? Optional.of(grid[coord.row][coord.col]) : Optional.empty();
    }

    public Optional<Pipe> getPipe(int row, int col) {
      return getPipe(new Coordinate(row, col));
    }

    private boolean isValid(Coordinate coordinate) {
      return coordinate.row >= 0 && coordinate.col >= 0 && coordinate.row < nRows && coordinate.col < nCols;
    }

    private void adjustStartType() {
      if (start != null) {
        PipeType newType = Arrays.stream(PipeType.values())
            .filter(list -> list.getMovements().size() == start.getAllowed().size())
            .filter(list -> list.getMovements().containsAll(start.getAllowed()))
            .findFirst()
            .orElse(PipeType.START);
        start.setType(newType);
      }
    }
  }

  @Override
  public Object part1() {
    PipeGrid grid = new PipeGrid(dayGrid());
    findLoop(grid);

    return Arrays.stream(grid.getGrid())
        .flatMap(Arrays::stream)
        .filter(Pipe::isVisited)
        .mapToInt(Pipe::getDistance)
        .summaryStatistics()
        .getMax();
  }

  private static void findLoop(PipeGrid grid) {
    Queue<Pipe> queue = new LinkedList<>();
    queue.add(grid.start);
    while (! queue.isEmpty()) {
      Pipe actual = queue.remove();

      if (actual.isVisited()) continue;
      actual.setVisited(true);
      Stream.of(Coordinate.GO_NORTH, Coordinate.GO_SOUTH, Coordinate.GO_EAST, Coordinate.GO_WEST)
          .filter(movement -> actual.getAllowed().contains(movement))
          .map(movement -> grid.getPipe(actual.getCoord().add(movement)).orElse(null))
          .filter(Objects::nonNull)
          .filter(not(Pipe::isVisited))
          .forEach(pipe -> {
            pipe.setDistance(actual.getDistance() + 1);
            queue.add(pipe);
          });
    }
  }

  @Override
  public Object part2() {
    PipeGrid grid = new PipeGrid(dayGrid());
    findLoop(grid);

    // remove non-loop pipes
    Arrays.stream(grid.getGrid())
        .flatMap(Arrays::stream)
        .filter(not(Pipe::isVisited))
        .forEach(pipe -> { pipe.setType(PipeType.GROUND); });

    int internalCount = 0;
    for (int row = 0; row < grid.nRows; row++) {
      boolean isInternal = false;
      PipeType seen = PipeType.GROUND;
      for (int col = 0; col < grid.nCols; col++) {
        Pipe actual = grid.getPipe(row, col).orElse(null);
        switch (Objects.requireNonNull(actual).getType()) {
          case GROUND -> { // '.'
            if (isInternal) internalCount = internalCount + 1;
          }
          case NORTH_SOUTH  -> {  // '|' changes from inside to outside
            isInternal = ! isInternal;
          }
          case NORTH_EAST -> { // 'L' save for latest check
            seen = PipeType.NORTH_EAST;
          }
          case SOUTH_EAST -> { // 'F' save for latest check
            seen = PipeType.SOUTH_EAST;
          }
          case NORTH_WEST -> { // 'J' changes if previous 'F': 'F---J'
            if (seen == PipeType.SOUTH_EAST) isInternal = ! isInternal;
          }
          case SOUTH_WEST -> { // '7' change if previous 'L': 'L---7'
            if (seen == PipeType.NORTH_EAST ) isInternal = ! isInternal;
          }
        }
      }
    }
    return internalCount;
  }
}
