package org.jpl.advent.year23.days;

import static java.lang.Character.getNumericValue;
import static java.lang.Character.isDigit;
import static java.lang.Math.max;
import static java.lang.Math.min;
import org.jpl.advent.year23.Day2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day3 extends Day2023 {
  public Day3() {
    super(3);
  }

  public static void main(String[] args) {
    new Day3().printParts();
  }

  @Override
  public Object part1() {

    char[][] grid = dayGrid();
    int total = 0;

    for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
      char[] row = grid[rowIndex];
      int colIndex = 0;
      while (colIndex < row.length) {
        if (isDigit(row[colIndex])) { // number start
          int endIndex = colIndex + 1;
          int value = getNumericValue(row[colIndex]);
          while (endIndex < row.length && isDigit(row[endIndex])) {
            value = value * 10 + getNumericValue(row[endIndex]);
            endIndex++;
          }
          // number ends, test if is adjacent to any symbol
          if (isAdjacentToSymbol(grid, rowIndex, colIndex - 1, endIndex)) {
            total += value;
          }
          colIndex = endIndex - 1;
        }
        colIndex++;
      }
    }

    return total;
  }

  private boolean isAdjacentToSymbol(char[][] grid, int rowIndex, int colStart, int colEnd) {
    for (int r = max(rowIndex - 1, 0); r <= min(rowIndex + 1, grid.length - 1); r++) {
      for (int c = max(colStart, 0); c <= min(colEnd, grid[r].length - 1); c++) {
        if ((grid[r][c] != '.') && !isDigit(grid[r][c])) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public Object part2() {

    char[][] grid = dayGrid();
    Map<String, List<Integer>> asterisksMap = new HashMap<>();

    for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
      char[] row = grid[rowIndex];
      int colIndex = 0;
      while (colIndex < row.length) {
        if (isDigit(row[colIndex])) { // number start
          int endIndex = colIndex + 1;
          int value = getNumericValue(row[colIndex]);
          while (endIndex < row.length && isDigit(row[endIndex])) {
            value = value * 10 + getNumericValue(row[endIndex]);
            endIndex++;
          }
          // number ends, find asterisks and add values to map
          findAsterisks(grid, rowIndex, colIndex - 1, endIndex, asterisksMap, value);
          colIndex = endIndex - 1;
        }
        colIndex++;
      }
    }

    return asterisksMap.values().stream()
        .filter(list -> list.size() == 2)
        .mapToInt(list -> list.get(0) * list.get(1))
        .sum();
  }

  private void findAsterisks(char[][] grid, int rowIndex, int colStart, int colEnd, Map<String, List<Integer>> map, int value) {
    for (int r = max(rowIndex - 1, 0); r <= min(rowIndex + 1, grid.length - 1); r++) {
      for (int c = max(colStart, 0); c <= min(colEnd, grid[r].length - 1); c++) {
        if (grid[r][c] == '*') {
          String key = "$%d$%d".formatted(r, c);
          List<Integer> list = map.getOrDefault(key, new ArrayList<Integer>());
          list.add(value);
          map.put(key, list);
        }
      }
    }
  }

}
