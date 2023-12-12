package org.jpl.advent.year23.days;

import org.jpl.advent.year23.Day2023;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day12 extends Day2023 {
  public Day12() {
    super(12);
  }

  public static void main(String[] args) {
    new Day12().printParts();
  }

  @Override
  public Object part1() {
    return dayStream()
        .mapToLong(input -> {
          String[] parts = input.split(" ");
          List<Integer> sequences = Arrays.stream(parts[1].split(",")).map(Integer::parseInt).toList();
          return countPossibles(parts[0], sequences, new HashMap<>());
        })
        .sum();
  }

  @Override
  public Object part2() {

    Map<String, Long> memory = new HashMap<>();

    return dayStream()
        .mapToLong(input -> {
          String[] parts = input.split(" ");
          String springs = replicate5(parts[0], "?");
          List<Integer> sequences = Arrays.stream(replicate5(parts[1], ",").split(",")).map(Integer::parseInt).toList();
          return countPossibles(springs, sequences, memory);
        })
        .sum();
  }

  private long countPossibles(String springs, List<Integer> sequences, Map<String, Long> memory) {

    // check in cached results
    String key = generateKey(springs, sequences);
    if (memory.containsKey(key)) {
      return memory.get(key);
    }

    //System.out.println("testing " + springs);
    if (springs.isEmpty()) { // no more springs, check that all sequences has been consumed
      long result = sequences.isEmpty() ? 1 : 0;
      memory.put(key, result);
      return result;
    }
    if (springs.startsWith("?")) { // adds both possible substitutions
      long dot = countPossibles("." + springs.substring(1), sequences, memory);
      long hash = countPossibles("#" +springs.substring(1), sequences, memory);
      memory.put(key, dot + hash);
      return dot+hash;
    }
    if (springs.startsWith(".")) { // ignore dots, just advance
      long result = countPossibles(springs.substring(1), sequences, memory);
      memory.put(key, result);
      return result;
    }
    if (springs.startsWith("#")) { // look for a valid sequence of # or ?
      if (sequences.isEmpty()) { // more springs than sequences
        memory.put(key, 0L);
        return 0;
      }
      int length = sequences.get(0);
      if (sequenceIsShorterThanExpected(springs, length)) { // sequence of # or ? is short
        memory.put(key, 0L);
        return 0;
      }
      if (sequences.size() > 1) { // more sequences ahead
        if (! hasSeparatorAfterSequence(springs, length)) { // check actual sequence is separated by '.' or '?'
          memory.put(key, 0L);
          return 0;
        }
        // actual sequence completed, advance to next sequence
        String nextSprings = springs.substring(length + 1);
        List<Integer> nextSequence = sequences.stream().skip(1).toList();
        long result = countPossibles(nextSprings, nextSequence, memory);
        memory.put(key, result);
        return result;
      }
      // this is last sequence, but need to check trailing '.' or '?'
      String nextSprings = springs.substring(length);
      List<Integer> nextSequence = sequences.stream().skip(1).toList();
      long result = countPossibles(nextSprings, nextSequence, memory);
      memory.put(key, result);
      return result;
    }
    memory.put(key, 0L);
    return 0;
  }

  private boolean sequenceIsShorterThanExpected(String springs, int length) {
    return springs.length() < length || springs.substring(0, length).contains(".");
  }

  private boolean hasSeparatorAfterSequence(String springs, int length) {
    return springs.length() > length + 1 && springs.charAt(length) != '#';
  }

  private String generateKey(String springs, List<Integer> sequences) {
    return "%s%s".formatted(springs, sequences.toString());
  }

  private String replicate5(String original, String joiner) {
    return IntStream.range(0, 5).boxed().map(n -> original).collect(Collectors.joining(joiner));
  }

}
