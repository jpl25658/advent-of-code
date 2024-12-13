package org.jpl.advent.year24.days;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jpl.advent.common.Coord;
import org.jpl.advent.common.Grid;
import org.jpl.advent.year24.Day2024;

public class Day12 extends Day2024 {
  public Day12() {
    super(12);
  }

  public static void main(String[] args) {
    new Day12().printParts();
  }

  @Override
  public Object part1() {
    Grid garden = parseInput();
    List<Region> regions = calcRegions(garden);

    return regions.stream()
        .mapToLong(region -> region.plots().size() * calcPerimeter(region, garden))
        .sum();
  }

  @Override
  public Object part2() {
    var garden = parseInput();

    return false;
  }

  private static final List<Coord> MOVES = List.of(Coord.N, Coord.S, Coord.E, Coord.W);

  private record Region(char flower, Set<Coord> plots) {
  }

  private Grid parseInput() {
    return new Grid(dayGrid());

  }

  private List<Region> calcRegions(Grid garden) {
    var visited = new boolean[garden.rowLength()][garden.colLength()];
    List<Region> regions = new ArrayList<>();

    for (var row = 0; row < garden.rowLength(); row++) {
      for (var col = 0; col < garden.colLength(); col++) {
        if (!visited[row][col]) {
          regions.add(getRegion(garden, visited, new Coord(row, col)));
        }
      }
    }
    return regions;
  }

  private Region getRegion(Grid garden, boolean[][] visited, Coord start) {
    var flower = garden.get(start);
    var plots = new HashSet<Coord>();
    Deque<Coord> pending = new ArrayDeque<>();
    pending.add(start);

    while (!pending.isEmpty()) {
      Coord current = pending.poll();
      if (garden.get(current) == flower && !visited[current.row()][current.col()]) {
        visited[current.row()][current.col()] = true;
        plots.add(current);
        MOVES.stream()
            .map(current::add)
            .forEach(pending::push);
      }
    }
    return new Region(flower, plots);
  }

  private long calcPerimeter(Region region, Grid garden) {
    return region.plots().stream()
        .mapToLong(plot -> findBorders(garden, region.flower(), plot))
        .sum();
  }

  private long findBorders(Grid garden, char flower, Coord coord) {
    return MOVES.stream()
        .map(move -> garden.get(coord.add(move)))
        .filter(plot -> plot != flower)
        .count();
  }

}
