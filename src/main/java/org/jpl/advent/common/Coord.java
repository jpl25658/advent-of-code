package org.jpl.advent.common;

public record Coord(int row, int col) {

  public Coord add(Coord other) {
    return new Coord(row + other.row, col + other.col);
  }

  public static Coord N = new Coord(-1, 0);
  public static Coord S = new Coord(1, 0);
  public static Coord E = new Coord(0, 1);
  public static Coord W = new Coord(0, -1);
  public static Coord NE = N.add(E);
  public static Coord NW = N.add(W);
  public static Coord SE = S.add(E);
  public static Coord SW = S.add(W);
}
