package org.jpl.advent.year24.days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.jpl.advent.common.Coord;
import org.jpl.advent.common.Grid;
import org.jpl.advent.common.Pair;
import org.jpl.advent.year24.Day2024;

public class Day8 extends Day2024 {

  public Day8() {
    super(8);
  }

  public static void main(String[] args) {
    new Day8().printParts();
  }

  @Override
  public Object part1() {
    var grid = parseInput();

    return extractFrequencies(grid).entrySet().stream()
        .flatMap(entry -> findAntinodes(grid, entry.getValue()))
        .collect(Collectors.toSet())
        .size();
  }

  @Override
  public Object part2() {
    var grid = parseInput();

    return extractFrequencies(grid).entrySet().stream()
        .flatMap(entry -> findAntinodesWithResonance(grid, entry.getValue()))
        .collect(Collectors.toSet())
        .size();
  }

  private Grid parseInput() {
    return new Grid(dayGrid());
  }

  private Map<Character, List<Coord>> extractFrequencies(Grid grid) {
    var frequencies = new HashMap<Character, List<Coord>>();
    for (var row = 0; row < grid.rowLength(); row++) {
      for (var col = 0; col < grid.rowLength(); col++) {
        var coord = new Coord(row, col);
        if (grid.get(coord) != Grid.EMPTY) {
          var frequency = grid.get(coord);
          var antennas = frequencies.getOrDefault(frequency, new ArrayList<>());
          antennas.add(coord);
          frequencies.put(frequency, antennas);
        }
      }
    }
    return frequencies;
  }

  private Stream<Coord> findAntinodes(Grid grid, List<Coord> antennas) {
    var antinodes = new HashSet<Coord>();
    getAntennaPairs(antennas).forEach(pair -> {
      var offset = pair.a().sub(pair.b());
      antinodes.add(pair.a().add(offset));
      antinodes.add(pair.b().add(offset.inverse()));
    });
    return antinodes.stream().filter(grid::isValid);
  }

  private List<Pair<Coord, Coord>> getAntennaPairs(List<Coord> antennas) {
    return IntStream.range(0, antennas.size()).boxed()
        .flatMap(i -> IntStream.range(i + 1, antennas.size()).mapToObj(j -> new Pair<>(antennas.get(i), antennas.get(j))))
        .toList();
  }

  private Stream<Coord> findAntinodesWithResonance(Grid grid, List<Coord> antennas) {
    var antinodes = new HashSet<Coord>();
    getAntennaPairs(antennas).forEach(pair -> {
      var offset = pair.a().sub(pair.b());
      antinodes.addAll(findResonances(grid, pair.a(), offset));
      antinodes.addAll(findResonances(grid, pair.b(), offset.inverse()));
    });
    return antinodes.stream();
  }

  private List<Coord> findResonances(Grid grid, Coord start, Coord offset) {
    var antinodes = new ArrayList<Coord>();
    var antinode = new Coord(start.row(), start.col());
    while (grid.get(antinode) != Grid.OUT_GRID) {
      antinodes.add(antinode);
      antinode = antinode.add(offset);
    }
    return antinodes;
  }

}
