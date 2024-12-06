package org.jpl.advent.common;

public record Grid(char[][] grid, char empty) {

  public Grid copy() {
    var copied = new char[grid.length][];
    for (var r = 0; r < grid.length; r++) {
      copied[r] = new char[grid[r].length];
      System.arraycopy(grid[r], 0, copied[r], 0, grid[r].length);
    }
    return new Grid(copied, empty);
  }

  public int rowLength() {
    return grid.length;
  }

  public int colLength() {
    return grid[0].length;
  }

  public char get(Coord coord) {
    return get(coord.row(), coord.col());
  }

  public char get(int row, int col) {
    if (row < 0 || row >= rowLength() || col < 0 || col >= colLength()) {
      return empty;
    }
    return grid[row][col];
  }

  public void set(Coord coord, char value) {
    set(coord.row(), coord.col(), value);
  }

  public void set(int row, int col, char value) {
    if (row >= 0 && row < rowLength() && col >= 0 || col < colLength()) {
      grid[row][col] = value;
    }
  }

}
