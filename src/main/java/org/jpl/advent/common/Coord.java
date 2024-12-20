package org.jpl.advent.common;

import java.util.List;

public record Coord(int row, int col) {

  public Coord add(Coord other) {
    return new Coord(row + other.row, col + other.col);
  }

  public Coord sub(Coord other) {
    return new Coord(row - other.row, col - other.col);
  }

  public Coord abs() {
    return new Coord(Math.abs(row), Math.abs(col));
  }

  public Coord inverse() {
    return new Coord(-row, -col);
  }

  public Coord wrap(Coord size) {
    return new Coord((row + size.row()) % size.row(), (col + size.col()) % size.col());
  }

  public int manhattan(Coord other) {
    return Math.abs(row - other.row()) + Math.abs(col - other.col());
  }

  public static Coord N = new Coord(-1, 0);
  public static Coord S = new Coord(1, 0);
  public static Coord E = new Coord(0, 1);
  public static Coord W = new Coord(0, -1);
  public static Coord NE = N.add(E);
  public static Coord NW = N.add(W);
  public static Coord SE = S.add(E);
  public static Coord SW = S.add(W);

  public static final List<Coord> MOVES = List.of(Coord.N, Coord.S, Coord.E, Coord.W);
  public static final List<Coord> HORIZONTAL = List.of(Coord.E, Coord.W);
  public static final List<Coord> VERTICAL = List.of(Coord.N, Coord.S);


}
