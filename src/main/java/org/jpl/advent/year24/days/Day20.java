package org.jpl.advent.year24.days;

import static org.jpl.advent.common.Coord.MOVES;
import static org.jpl.advent.common.Grid.EMPTY;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jpl.advent.common.Coord;
import org.jpl.advent.common.Grid;
import org.jpl.advent.year24.Day2024;

public class Day20 extends Day2024 {
  public Day20() {
    super(20);
  }

  public static void main(String[] args) {
    new Day20().printParts();
  }

  @Override
  public Object part1() {
    var grid = parseInput();
    var start = findCoord(grid, START);
    var end = findCoord(grid, END);

    var visited = traverseMaze(grid, start, end);
    var ordered = new TreeMap<>(visited.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)));

    var savings = ordered.values().stream()
        .flatMap(coord -> findCheats(coord, grid, visited))
        .map(Cheat::saving)
        .collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));

    var listing = savings.entrySet().stream()
        .map(entry -> printSaving(entry.getKey(), entry.getValue()))
        .collect(Collectors.joining("\n"));

    var atLeastHundred = savings.entrySet().stream()
        .filter(entry -> entry.getKey() >= 100)
        .mapToLong(Map.Entry::getValue)
        .sum();

    return listing + "\n\nAt least 100 saving: %d".formatted(atLeastHundred);
  }

  private Map<Coord, Integer> traverseMaze(Grid grid, Coord start, Coord end) {
    Map<Coord, Integer> visited = new HashMap<>();
    var picoSeconds = 0;
    var actual = start;
    visited.put(actual, picoSeconds);
    while (!actual.equals(end)) {
      actual = MOVES.stream()
          .map(actual::add)
          .filter(c -> !visited.containsKey(c))
          .filter(c -> grid.get(c) != WALL)
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("dead end"));
      picoSeconds++;
      visited.put(actual, picoSeconds);
    }
    return visited;
  }

  private record Cheat(Coord from, Coord to, int saving) {
  }

  private Stream<Cheat> findCheats(Coord actual, Grid grid, Map<Coord, Integer> visited) {
    return MOVES.stream()
        .filter(move -> grid.get(actual.add(move)) == WALL)
        .map(move -> move.add(move))
        .filter(move2 -> grid.get(actual.add(move2)) == EMPTY || grid.get(actual.add(move2)) == END)
        .map(move2 -> createCheat(actual, actual.add(move2), visited))
        .filter(cheat -> cheat.saving() > 0);
  }

  private Cheat createCheat(Coord from, Coord to, Map<Coord, Integer> visited) {
    return new Cheat(from, to, visited.get(to) - visited.get(from) - 2);
  }

  private String printSaving(int savedPicoseconds, long numCheats) {
    if (numCheats == 1) {
      return "There is one cheat that saves %d picoseconds.".formatted(savedPicoseconds);
    }
    return "There are %d cheats that save %d picoseconds.".formatted(numCheats, savedPicoseconds);
  }


  @Override
  public Object part2() {
    var input = parseInput();

    return false;
  }

  private final char WALL = '#';
  private final char START = 'S';
  private final char END = 'E';

  private Grid parseInput() {
    return new Grid(dayGrid(), EMPTY, WALL);
  }

  private Coord findCoord(Grid grid, char value) {
    for (var r = 0; r < grid.rowLength(); r++) {
      for (var c = 0; c < grid.colLength(); c++) {
        if (grid.get(r, c) == value) {
          return new Coord(r, c);
        }
      }
    }
    throw new IllegalStateException("Robot not found");
  }


}
