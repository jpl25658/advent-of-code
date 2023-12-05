package org.jpl.advent.year23.days.day5;

import org.jpl.advent.common.Pair;
import org.jpl.advent.year23.Day2023;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day5 extends Day2023 {

  public Day5() {
    super(5);
  }

  public static void main(String[] args) {
    new Day5().printParts();
  }

  private List<Long> seeds;
  private Map<String, Mapper> mappers;

  private void initSeedsAndMappers() {
    List<String> parts = new java.util.ArrayList<>(Arrays.stream(day().split("\n\n")).toList());
    seeds = Parser.parseSeeds(parts.get(0));
    parts.remove(0);
    mappers = parts.stream()
        .map(Parser::parseMapper)
        .collect(Collectors.toMap(Mapper::getSource, map -> map));
  }

  @Override
  public Object part1() {
    initSeedsAndMappers();

    return seeds.stream().mapToLong(this::applyMappers).min().getAsLong();
  }

  private Long applyMappers(Long seed) {
    long result = seed;
    Mapper currentMap = mappers.get("seed");
    while (true) {
      if ("location".equals(currentMap.getDestination())) break;
      result = currentMap.map(result);
      currentMap = mappers.get(currentMap.getDestination());
    }
    return currentMap.map(result);
  }

  @Override
  public Object part2() {
    initSeedsAndMappers();

    List<Pair<Long, Long>> seedRanges = IntStream.range(0, seeds.size() / 2)
        .mapToObj(i -> seeds.subList(i * 2, i * 2 + 2))
        .map(pairList -> new Pair<>(pairList.get(0), pairList.get(0) + pairList.get(1)))
        .toList();

    return seedRanges.parallelStream().mapToLong(this::getMinForRange).min().getAsLong();
  }

  private Long getMinForRange(Pair<Long, Long> pair) {
    return LongStream.range(pair.getLeft(), pair.getRight()).map(this::applyMappers).min().getAsLong();
  }

}
