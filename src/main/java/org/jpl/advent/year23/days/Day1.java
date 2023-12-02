package org.jpl.advent.year23.days;

import org.jpl.advent.year23.Day2023;

public class Day1 extends Day2023 {
  public Day1() {
    super(1);
  }

  public static void main(String[] args) {
    new Day1().printParts();
  }

  @Override
  public Object part1() {
    return dayStream().mapToInt(line -> {
      String[] digits = line.replaceAll("\\D", "").split("");
      return Integer.parseInt(digits[0]) * 10 + Integer.parseInt(digits[digits.length - 1]);
    }).sum();
  }

  @Override
  public Object part2() {
    return dayStream().mapToInt(line -> getFirstDigit(line) * 10 + getLastDigit(line)).sum();
  }

  private record Number(int value, String str1, String str2){}

  private static final Number[] numbers = {
          new Number(1, "one", "1"),
          new Number(2, "two", "2"),
          new Number(3, "three", "3"),
          new Number(4, "four", "4"),
          new Number(5, "five", "5"),
          new Number(6, "six", "6"),
          new Number(7, "seven", "7"),
          new Number(8, "eight", "8"),
          new Number(9, "nine", "9")
  };
  private int getFirstDigit(String day) {
    String s = day;
    while (!s.isEmpty()) {
      for(Number n : numbers) {
        if (s.startsWith(n.str1) || s.startsWith(n.str2)) {
          return n.value;
        }
      }
      s = s.substring(1);
    }
    return 0;
  }

  private int getLastDigit(String day) {
    String s = day;
    while (!s.isEmpty()) {
      for(Number n : numbers) {
        if (s.endsWith(n.str1) || s.endsWith(n.str2)) {
          return n.value;
        }
      }
      s = s.substring(0, s.length() - 1);
    }
    return 0;
  }

}
