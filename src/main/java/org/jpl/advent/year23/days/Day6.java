package org.jpl.advent.year23.days;

import org.jpl.advent.year23.Day2023;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day6 extends Day2023 {
  public Day6() {
    super(6);
  }

  public static void main(String[] args) {
    new Day6().printParts();
  }

  record Game(Long time, Long best) {
    public Long score(Long n) {
      return n * (time - n);
    }
    public boolean isBetter(Long n) {
      return score(n) > best;
    }
  }

  public List<Long> parseList(String string) {
    return Arrays.stream(string.split(":")[1].trim().split("\\s++")).map(Long::valueOf).toList();
  }
  
  public Long parseLong(String string) {
    return Long.valueOf(string.split(":")[1].trim());
  }

  @Override
  public Object part1() {

    String[] input = dayStrings();
    List<Long> times = parseList(input[0]);
    List<Long> bests = parseList(input[1]);

    List<Game> games = IntStream.range(0, times.size())
        .mapToObj(i -> new Game(times.get(i), bests.get(i)))
        .toList();

    return games.stream().mapToLong(this::calcWins).reduce(1L, Math::multiplyExact);
  }

  private long calcWins(Game game) {
    return LongStream.range(1, game.time).filter(game::isBetter).count();
  }

  @Override
  public Object part2() {

    String[] input = dayStrings();
    Long time = parseLong(input[0].replaceAll("\\s+", ""));
    Long best = parseLong(input[1].replaceAll("\\s+", ""));

    Game game = new Game(time, best);

    long firstWin = LongStream.range(1, game.time).filter(game::isBetter).findFirst().orElse(game.time);
    long lastWin = LongStream.iterate(game.time - 1, n -> n - 1).filter(game::isBetter).findFirst().orElse(1L);

    return lastWin - firstWin + 1;
  }

}
