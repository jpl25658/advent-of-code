package org.jpl.advent.year23.days;

import org.jpl.advent.year23.Day2023;
import static org.jpl.advent.year23.days.Day16.Direction.EAST;
import static org.jpl.advent.year23.days.Day16.Direction.NORTH;
import static org.jpl.advent.year23.days.Day16.Direction.SOUTH;
import static org.jpl.advent.year23.days.Day16.Direction.WEST;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day16 extends Day2023 {
  public Day16() {
    super(16);
  }

  public static void main(String[] args) {
    new Day16().printParts();
  }

  private static final char EMPTY = '.';
  private static final char MIRROR_R = '/';
  private static final char MIRROR_L = '\\';
  private static final char SPLITTER_V = '|';
  private static final char SPLITTER_H = '-';

  enum Direction {
    NORTH(-1, 0),
    SOUTH(1, 0),
    WEST(0, -1),
    EAST(0, 1);

    private final int row;
    private final int col;

    Direction(int row, int col) {
      this.row = row;
      this.col = col;
    }
  };

  private record Beam(int row, int col, Direction dir) {};

  private static class Tile {
    private final char tile;
    private final int row;
    private final int col;
    private final Set<Direction> interactions;

    public Tile(char tile, int row, int col) {
      this.tile = tile;
      this.row = row;
      this.col = col;
      this.interactions = new HashSet<>();
    }

    public boolean isVisited() {
      return ! this.interactions.isEmpty();
    }
  }

  char[][] dayData;
  int rowMax;
  int colMax;

  @Override
  public Object part1() {
    dayData = dayGrid();
    rowMax = dayData.length;
    colMax = dayData[0].length;

    return interact(0, 0, EAST);

  }

  @Override
  public Object part2() {
    dayData = dayGrid();
    rowMax = dayData.length;
    colMax = dayData[0].length;

    return Stream.of(
            IntStream.range(0, colMax).mapToObj(col -> interact(0, col, Direction.SOUTH)),
            IntStream.range(0, colMax).mapToObj(col -> interact(rowMax - 1, col, Direction.NORTH)),
            IntStream.range(0, rowMax).mapToObj(row -> interact(row, 0, Direction.EAST)),
            IntStream.range(0, rowMax).mapToObj(row -> interact(row, colMax - 1, Direction.WEST))
        )
        .flatMap(Function.identity())
        .max(Long::compareTo)
        .orElse(0L);
  }

  private long interact(int row, int col, Direction initial) {
    Tile[][] contraption = new Tile[rowMax][colMax];
    Arrays.setAll(contraption, r -> IntStream.range(0, colMax).mapToObj(c -> new Tile(dayData[r][c], r, c)).toArray(Tile[]::new));
    Stack<Beam> beams= new Stack<>();

    traverseTile(contraption[row][col], initial, beams);
    while (! beams.isEmpty()) {
      Beam beam = beams.pop();
      int nextRow = beam.row + beam.dir.row;
      int nextCol = beam.col + beam.dir.col;
      if ( nextRow >= 0 && nextRow < rowMax && nextCol >= 0 && nextCol < colMax) {
        traverseTile(contraption[nextRow][nextCol], beam.dir, beams);
      }
    }
    return Stream.of(contraption).flatMap(Arrays::stream).filter(Tile::isVisited).count();
  }

  private void traverseTile(Tile tile, Direction direction, Stack<Beam> beams) {
    if (tile.interactions.contains(direction) && tile.tile != EMPTY) {
      return;
    }
    tile.interactions.add(direction);

    if (tile.tile == EMPTY) {
      beams.add(new Beam(tile.row, tile.col, direction));
    }
    if (tile.tile == SPLITTER_H) {
      if (direction == NORTH || direction == SOUTH) {
        beams.push(new Beam(tile.row, tile.col, WEST));
        beams.push(new Beam(tile.row, tile.col, EAST));
      } else {
        beams.push(new Beam(tile.row, tile.col, direction));
      }
    }
    if (tile.tile == SPLITTER_V) {
      if (direction == WEST || direction == EAST) {
        beams.push(new Beam(tile.row, tile.col, NORTH));
        beams.push(new Beam(tile.row, tile.col, SOUTH));
      } else {
        beams.push(new Beam(tile.row, tile.col, direction));
      }
    }
    if (tile.tile == MIRROR_R) {
      if (direction == SOUTH) beams.push(new Beam(tile.row, tile.col, WEST));
      if (direction == NORTH) beams.push(new Beam(tile.row, tile.col, EAST));
      if (direction == EAST) beams.push(new Beam(tile.row, tile.col, NORTH));
      if (direction == WEST) beams.push(new Beam(tile.row, tile.col, SOUTH));
    }
    if (tile.tile == MIRROR_L) {
      if (direction == SOUTH ) beams.push(new Beam(tile.row, tile.col, EAST));
      if (direction == NORTH ) beams.push(new Beam(tile.row, tile.col, WEST));
      if (direction == EAST ) beams.push(new Beam(tile.row, tile.col, SOUTH));
      if (direction == WEST ) beams.push(new Beam(tile.row, tile.col, NORTH));
    }
  }

}
