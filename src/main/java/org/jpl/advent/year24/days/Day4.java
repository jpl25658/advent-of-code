package org.jpl.advent.year24.days;

import java.util.List;
import org.jpl.advent.year24.Day2024;

public class Day4 extends Day2024 {
  public Day4() {
    super(4);
  }

  public static void main(String[] args) {
    new Day4().printParts();
  }

  @Override
  public Object part1() {
    var grid = parseInput();
    List<Coord> offsets = List.of(Coord.N, Coord.NE, Coord.E, Coord.SE, Coord.S, Coord.SW, Coord.W, Coord.NW);
    var found = 0L;

    for (var row = 0; row < grid.rowLength(); row++) {
      for (var col = 0; col < grid.colLength(); col++) {
        if (grid.get(row, col) == 'X') {
          Coord actual = new Coord(row, col);
          found += offsets.stream().filter(offset -> findXmas(grid, actual, offset)).count();
        }
      }
    }

    return found;
  }

  @Override
  public Object part2() {
    var grid = parseInput();
    var found = 0;

    for (var row = 0; row < grid.rowLength(); row++) {
      for (var col = 0; col < grid.colLength(); col++) {
        if (grid.get(row, col) == 'A') {
          var actual = new Coord(row, col);
          if (findMaS(grid, actual, Coord.NW, Coord.SE) && findMaS(grid, actual, Coord.NE, Coord.SW)) {
            found++;
          }
        }
      }
    }

    return found;
  }

  private Grid parseInput() {
    return new Grid(dayGrid(), '.');
  }

  private record Coord(int row, int col) {

    Coord add(Coord other) {
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

  private record Grid(char[][] grid, char empty) {

    public int rowLength() { return grid.length; }

    public int colLength() { return grid[0].length; }

    public char get(Coord coord) {
      return get(coord.row, coord.col);
    }

    public char get(int row, int col) {
      if (row < 0 || row >= rowLength() || col < 0 || col >= colLength()) {
        return empty;
      }
      return grid[row][col];
    }
  }

  private boolean findXmas(Grid grid, Coord origen, Coord offset) {
    var actual = origen;

    for (var letter : new char[]{'X', 'M', 'A', 'S'}) {
      if (letter != grid.get(actual)) {
        return false;
      }
      actual = actual.add(offset);
    }

    return true;
  }

  private boolean findMaS(Grid grid, Coord origen, Coord offset1, Coord offset2)  {
    var letter1 = grid.get(origen.add(offset1));
    var letter2 = grid.get(origen.add(offset2));

    return (letter1 == 'M' && letter2 == 'S') || (letter1 == 'S' && letter2 == 'M');
  }

}
