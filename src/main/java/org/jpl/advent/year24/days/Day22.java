package org.jpl.advent.year24.days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jpl.advent.year24.Day2024;

public class Day22 extends Day2024 {
  public Day22() {
    super(22);
  }

  public static void main(String[] args) {
    new Day22().printParts();
  }

  @Override
  public Object part1() {
    var numbers = parseInput();

    return numbers.stream()
        .mapToLong(this::calcSecret)
        .sum();
  }

  @Override
  public Object part2() {
    var numbers = parseInput();

    Map<String, Long> map = new HashMap<>();
    for (var number : numbers) {
      calcDifferences(number)
          .forEach((key, value) -> map.put(key, map.getOrDefault(key, 0L) + value));

    }

    return map.values().stream().max(Long::compare).orElse(0L);
  }

  private List<Long> parseInput() {
    return dayNumberStream().boxed().toList();
  }

  private long calcSecret(long n) {
    var x = n;
    for (var i = 0; i < 2000; i++) {
      x = next(x);
    }
    return x;
  }

  private long next(long n) {
    var x = n;
    x = (x ^ x * 64) % 16777216;
    x = (x ^ x / 32) % 16777216;
    x = (x ^ x * 2048) % 16777216;
    return x;
  }

  private record PriceChange(long price, String change) {}

  private Map<String, Long> calcDifferences(long number) {
    Map<String, Long> map = new HashMap<>();
    var priceChanges = calcPriceChange(number);
    for (var i = 3; i < priceChanges.size(); i++) {
      var lastFour = IntStream.rangeClosed(i - 3, i).mapToObj(n -> priceChanges.get(n).change());
      var key = lastFour.collect(Collectors.joining("#"));
      map.putIfAbsent(key, priceChanges.get(i).price());
    }
    return map;
  }

  private List<PriceChange> calcPriceChange(long n) {
    List<PriceChange> result = new ArrayList<>();
    var x = n;
    var previousPrice = n % 10;
    for (var i = 0; i < 2000; i++) {
      x = next(x);
      var price = x % 10;
      result.add(new PriceChange(price, String.valueOf(price - previousPrice)));
      previousPrice = price;
    }
    return result;
  }

}
