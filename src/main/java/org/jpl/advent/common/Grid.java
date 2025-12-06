package org.jpl.advent.common;

import java.util.Arrays;
import java.util.stream.Collectors;

public record Grid(char[][] grid, char empty, char outGrid) {

  public static final char EMPTY = '.';
  public static final char OUT_GRID = '@';

  public Grid(char[][] grid) {
    this(grid, EMPTY, OUT_GRID);
  }

  public Grid(char[][] grid, char empty) {
    this(grid, empty, OUT_GRID);
  }

  public Grid copy() {
    var copied = new char[grid.length][];
    for (var r = 0; r < grid.length; r++) {
      copied[r] = grid[r].clone();
    }
    return new Grid(copied, empty, outGrid);
  }

  public int rowLength() {
    return grid.length;
  }

  public int colLength() {
    return grid[0].length;
  }

  public boolean isValid(Coord coord) {
    return isValid(coord.row(), coord.col());
  }

  public boolean isValid(int row, int col) {
    return row >= 0 && row < rowLength() && col >= 0 && col < colLength();
  }

  public char get(Coord coord) {
    return get(coord.row(), coord.col());
  }

  public char get(int row, int col) {
    return isValid(row, col) ? grid[row][col] : outGrid();
  }

  public void set(Coord coord, char value) {
    set(coord.row(), coord.col(), value);
  }

  public void set(int row, int col, char value) {
    if (isValid(row, col)) {
      grid[row][col] = value;
    }
  }

  public String print() {
    return Arrays.stream(grid)
        .map(String::new)
        .collect(Collectors.joining(System.lineSeparator()));
  }
}
