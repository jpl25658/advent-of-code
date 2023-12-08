package org.jpl.advent.year23.days;

import lombok.Builder;
import org.jpl.advent.common.Pair;
import org.jpl.advent.year23.Day2023;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

  @Builder
  private record Mapping(Long dest, Long src, Long range) {
    public Optional<Long> map(Long value) {
      return (value >= src && value < src + range) ? Optional.of(dest + value - src) : Optional.empty();
    }
  }

  @Builder
  private record Mapper(String source, String destination, List<Mapping> mappings) {
    public Long map(Long value) {
      return mappings.stream()
          .map(m -> m.map(value))
          .filter(Optional::isPresent)
          .findFirst()
          .orElse(Optional.empty())
          .orElse(value);
    }
  }

  private static class Parser {
    public static List<Long> parseSeeds(String input) {
      return Arrays.stream(input.split(": ")[1].split("\\s++")).map(Long::valueOf).toList();
    }

    public static Mapper parseMapper(String input) {
      List<String> parts = new java.util.ArrayList<>(Arrays.stream(input.split("\n")).toList());
      String[] header = parts.get(0).split(" ")[0].split("-to-");
      parts.remove(0);
      List<Mapping> mappings = parts.stream().map(Parser::parseMapping).toList();
      return Mapper.builder().source(header[0]).destination(header[1]).mappings(mappings).build();
    }

    private static Mapping parseMapping(String input) {
      long[] nums = Arrays.stream(input.split("\\s+")).mapToLong(Long::valueOf).toArray();
      return Mapping.builder().dest(nums[0]).src(nums[1]).range(nums[2]).build();
    }
  }


  private List<Long> seeds;
  private Map<String, Mapper> mappers;

  private void initSeedsAndMappers() {
    List<String> parts = new java.util.ArrayList<>(Arrays.stream(day().split("\n\n")).toList());
    seeds = Parser.parseSeeds(parts.get(0));
    parts.remove(0);
    mappers = parts.stream()
        .map(Parser::parseMapper)
        .collect(Collectors.toMap(Mapper::source, map -> map));
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
      if ("location".equals(currentMap.destination())) break;
      result = currentMap.map(result);
      currentMap = mappers.get(currentMap.destination());
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
    String pairString = "[%s-%s]".formatted(pair.getLeft(), pair.getRight());
    System.out.printf("%s start\n", pairString);
    long start = System.currentTimeMillis();
    Long result = LongStream.range(pair.getLeft(), pair.getRight()).map(this::applyMappers).min()
        .orElseThrow(() -> new IllegalStateException("Unexpected error mapping %s".formatted(pairString)));
    System.out.printf("%s ends (%d ms)\n", pairString, System.currentTimeMillis() - start);
    return result;
  }

}
