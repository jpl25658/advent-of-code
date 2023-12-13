package org.jpl.advent.year23.days;

import org.jpl.advent.year23.Day2023;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Day13 extends Day2023 {

  public Day13() {
    super(13);
  }

  public static void main(String[] args) {
    new Day13().printParts();
  }

  @Override
  public Object part1() {
    return Arrays.stream(day().split("\n\n")).mapToInt(pattern -> findAllReflections(pattern, 0)).sum();
  }

  @Override
  public Object part2() {
    return Arrays.stream(day().split("\n\n")).mapToInt(pattern -> findAllReflections(pattern, 1)).sum();
  }

  private int findAllReflections(String data, int expectedDifferences) {
    String[] horizontal = data.split("\n");
    String[] vertical = transpose(horizontal);
    return 100 * findReflections(horizontal, expectedDifferences) + findReflections(vertical, expectedDifferences);
  }

  private int findReflections(String[] lines, int expectedDifferences) {
    for (int i = 0; i < lines.length - 1; i++) {
      int differences = countDifferences(lines[i], lines[i + 1]);
      if (differences <= expectedDifferences) {
        boolean isMirror = true;
        for (int j = 1; j < Math.min(i, lines.length - i - 2) + 1; j++) {
          differences +=  countDifferences(lines[i - j], lines[i + 1 + j]);
          if (differences > expectedDifferences) {
            isMirror = false;
            break;
          }
        }
        if (isMirror && differences == expectedDifferences) {
          return i + 1;
        }
      }
    }
    return 0;
  }

  private int countDifferences(String one, String two) {
    int n = 0;
    for (int i = 0; i < one.length(); i++) {
      if (one.charAt(i) != two.charAt(i)) n++;
    }
    return n;
  }


  public String[] transpose(String[] values) {
    String[] transposed = new String[values[0].length()];
    for (int i = 0; i < transposed.length; i++) {
      StringBuilder builder = new StringBuilder();
      for (String value : values) {
        builder.append(value.charAt(i));
      }
      transposed[i] = builder.toString();
    }
    return transposed;
  }

  private void dump(String[] lines) {
    System.out.println("-----");
    IntStream.range(0, lines.length).boxed()
        .forEach(n -> {
          String binary = lines[n].replaceAll("\\.", "0").replaceAll("#", "1");
          int decimal = Integer.parseInt(binary, 2);
          System.out.println("%2d: %s - %-8d".formatted(n, binary, decimal));
        });
    System.out.println("-----");
  }

}