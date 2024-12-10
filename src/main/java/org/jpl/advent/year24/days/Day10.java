package org.jpl.advent.year24.days;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;
import org.jpl.advent.common.Coord;
import org.jpl.advent.common.Grid;
import org.jpl.advent.common.Pair;
import org.jpl.advent.year24.Day2024;

public class Day10 extends Day2024 {
  public Day10() {
    super(10);
  }

  public static void main(String[] args) {
    new Day10().printParts();
  }

  @Override
  public Object part1() {
    Grid grid = parseInput();

    return findTrailheads(grid).stream()
        .mapToInt(trailHead -> countHikingTrails(grid, trailHead).a())
        .sum();
  }

  @Override
  public Object part2() {
    Grid grid = parseInput();

    return findTrailheads(grid).stream()
        .mapToInt(trailHead -> countHikingTrails(grid, trailHead).b())
        .sum();
  }

  private Grid parseInput() {
    return new Grid(dayGrid());
  }

  private static final char START = '0';
  private static final char END = '9';
  private static final Coord[] MOVES = {Coord.N, Coord.S, Coord.E, Coord.W};

  private List<Coord> findTrailheads(Grid grid) {
    return IntStream.range(0, grid.rowLength()).mapToObj(row ->
            IntStream.range(0, grid.colLength())
                .filter(col -> grid.get(row, col) == '0')
                .mapToObj(col -> new Coord(row, col))
                .toList())
        .flatMap(List::stream)
        .toList();
  }

  private record State(char height, Coord pos) {
  }

  private Pair<Integer, Integer> countHikingTrails(Grid grid, Coord trailhead) {

    Deque<State> state = new ArrayDeque<>();
    state.add(new State(START, trailhead));
    var hikingTrails = new HashSet<Coord>();
    var rating = 0;
    while (!state.isEmpty()) {
      var actual = state.poll();
      if (actual.height() == END) {
        hikingTrails.add(actual.pos());
        rating++;
      } else {
        Arrays.stream(MOVES)
            .map(move -> actual.pos().add(move))
            .filter(coord -> grid.get(coord) == actual.height() + 1)
            .forEach(coord -> state.add(new State(grid.get(coord), coord)));
      }
    }

    return new Pair<>(hikingTrails.size(), rating);
  }

}
