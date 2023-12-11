package org.jpl.advent.year23.days;

import lombok.Setter;
import org.jpl.advent.common.Pair;
import org.jpl.advent.year23.Day2023;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.LongStream;

public class Day11 extends Day2023 {

  public Day11() {
    super(11);
  }

  public static void main(String[] args) {
    new Day11().printParts();
  }

  private static final String GALAXY = "#";

  private record Galaxy(long row, long col) {
    public long distance(Galaxy other) {
      return Math.abs(this.row - other.row) + Math.abs(this.col - other.col);
    }
  }

  private List<Galaxy> galaxies;
  private List<Long> emptyRows;
  private List<Long> emptyCols;

  @Override
  public Object part1() {
    readUniverse(dayStream().toList());
    List<Galaxy> expandedGalaxies = expandUniverse(2);

    return pairGalaxies(expandedGalaxies).stream()
        .mapToLong(pair -> pair.getLeft().distance(pair.getRight()))
        .sum();
  }
  @Override
  public Object part2() {
    readUniverse(dayStream().toList());
    List<Galaxy> expandedGalaxies = expandUniverse(getPart2Factor());

    return pairGalaxies(expandedGalaxies).stream()
        .mapToLong(pair -> pair.getLeft().distance(pair.getRight()))
        .sum();
  }

  @Setter
  private long testFactor = 0;

  private long getPart2Factor() {
    return  testFactor != 0 ? testFactor : 1_000_000;

  }

  private void readUniverse(List<String> input) {
    galaxies = new ArrayList<>();
    String[][] data = input.stream().map(line -> line.split("")).toArray(String[][]::new);
    for (int row = 0; row < data.length; row++) {
      for (int col = 0; col < data[row].length; col++) {
        if (GALAXY.equals(data[row][col])) {
          galaxies.add(new Galaxy(row, col));
        }
      }
    }
    emptyRows = LongStream.range(0, input.size())
        .filter(row -> galaxies.stream().noneMatch(g -> g.row == row))
        .boxed()
        .sorted(Comparator.reverseOrder())
        .toList();
    emptyCols = LongStream.range(0, input.get(0).length())
        .filter(col -> galaxies.stream().noneMatch(g -> g.col == col))
        .boxed()
        .sorted(Comparator.reverseOrder())
        .toList();
  }
  private List<Galaxy> expandUniverse(long factor) {
    return galaxies.stream()
        .map(g -> new Galaxy(
      g.row() + (factor - 1) * emptyRows.stream().filter(n -> n < g.row()).count(),
      g.col() + (factor - 1) * emptyCols.stream().filter(n -> n < g.col()).count()
        ))
        .toList();
  }

  private List<Pair<Galaxy, Galaxy>> pairGalaxies(List<Galaxy> universe) {
    List<Pair<Galaxy, Galaxy>> pairs = new ArrayList<>();

    for (int i = 0; i < universe.size() - 1; i++) {
      for (int j = i + 1; j < universe.size(); j++) {
        pairs.add(new Pair<>(universe.get(i), universe.get(j)));
      }
    }
    return pairs;
  }

}
