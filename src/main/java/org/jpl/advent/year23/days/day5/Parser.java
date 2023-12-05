package org.jpl.advent.year23.days.day5;

import java.util.Arrays;
import java.util.List;

public class Parser {

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
