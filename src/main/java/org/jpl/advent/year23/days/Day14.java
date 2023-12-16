package org.jpl.advent.year23.days;

import org.jpl.advent.year23.Day2023;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day14 extends Day2023 {
  public Day14() {
    super(14);
  }

  public static void main(String[] args) {
    new Day14().printParts();
  }

  public static final char ROCK = 'O';
  public static final char EMPTY = '.';
  public static final int NUM_CYCLES = 1_000_000_000;

  private record Cycle(int cycle, int load) {}

  @Override
  public Object part1() {
    char[][] grid = slideNorth(dayGrid());
    return totalLoad(grid);
  }

  @Override
  public Object part2() {
    char[][] grid = dayGrid();
    Map<String, Cycle> map = new HashMap<>();

    for (int cycle = 0; cycle < NUM_CYCLES; cycle++) {
      grid = slideEast(slideSouth(slideWest(slideNorth(grid))));
      String hash = calcHash(grid);
      if (! map.containsKey(hash)) {
        map.put(hash, new Cycle(cycle, totalLoad(grid)));
        continue;
      }
      int startCycle = map.get(hash).cycle;
      int loopLength = cycle - startCycle;
      int finalCycle = startCycle + (NUM_CYCLES - startCycle - 1) % loopLength;
      return map.values().stream().filter(c -> c.cycle == finalCycle).map(c -> c.load).findFirst().orElse(0);
    }
    return 0;
  }

  private char[][] slideNorth(char[][] grid) {
    for (int row = 1; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        if (grid[row][col] == ROCK) {
          int k = row - 1;
          while (grid[k][col] == EMPTY && k > 0) k--;
          if (k > 0 || grid[k][col] != EMPTY) k++;
          if (grid[k][col] == EMPTY) {
            grid[row][col] = EMPTY;
            grid[k][col] = ROCK;
          }
        }
      }
    }
    return grid;
  }

  private char[][] slideSouth(char[][] grid) {
    for (int row = grid.length - 2; row >= 0; row--) {
      for (int col = 0; col < grid[row].length; col++) {
        if (grid[row][col] == ROCK) {
          int k = row + 1;
          while (grid[k][col] == EMPTY && k < grid.length - 1) k++;
          if (k < grid.length - 1 || grid[k][col] != EMPTY) k--;
          if (grid[k][col] == EMPTY) {
            grid[row][col] = EMPTY;
            grid[k][col] = ROCK;
          }
        }
      }
    }
    return grid;
  }

  private char[][] slideWest(char[][] grid) {
    for (int row = 0; row < grid.length; row++) {
      for (int col = 1; col < grid[row].length; col++) {
        if (grid[row][col] == ROCK) {
          int k = col - 1;
          while (grid[row][k] == EMPTY && k > 0) k--;
          if (k > 0 || grid[row][k] != EMPTY) k++;
          if (grid[row][k] == EMPTY) {
            grid[row][col] = EMPTY;
            grid[row][k] = ROCK;
          }
        }
      }
    }
    return grid;
  }

  private char[][] slideEast(char[][] grid) {
    for (int row = 0; row < grid.length; row++) {
      for (int col = grid[row].length - 2; col >= 0; col--) {
        if (grid[row][col] == ROCK) {
          int k = col + 1;
          while (grid[row][k] == EMPTY && k < grid[0].length - 1) k++;
          if (k < grid[0].length - 1 || grid[row][k] != EMPTY) k--;
          if (grid[row][k] == EMPTY) {
            grid[row][col] = EMPTY;
            grid[row][k] = ROCK;
          }
        }
      }
    }
    return grid;
  }

  private int totalxLoad(char[][] grid) {
    int sum = 0;
    for (int i = 0; i < grid.length; i ++) {
      sum += (grid.length - i) * ((int) new String(grid[i]).chars().filter(c -> c == ROCK).count());
    }
    return sum;
  }

  private int totalLoad(char[][] grid) {
    return IntStream.range(0, grid.length)
        .mapToObj(i -> (grid.length - i) * new String(grid[i]).chars().filter(c -> c == ROCK).count())
        .mapToInt(Long::intValue)
        .sum();
  }


  private String calcHash(char[][] grid) {
    String txt = dump(grid);
    // return txt;

    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    byte[] hashedBytes = digest.digest(txt.getBytes());
    StringBuilder hashedString = new StringBuilder();
    for (byte b : hashedBytes) {
      hashedString.append(String.format("%02x", b));
    }
    return hashedString.toString();
  }

  private String dump(char[][] grid) {
    return Arrays.stream(grid).map(String::new).collect(Collectors.joining("\n"));
  }

}
