package org.jpl.advent.year23.days;

import org.jpl.advent.common.Pair;
import org.jpl.advent.year23.Day2023;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

public class Day4 extends Day2023 {

  public Day4() {
    super(4);
  }

  public static void main(String[] args) {
    new Day4().printParts();
  }

  record Card(Long cardId, List<Long> winningNumbers, List<Long> playedNumbers) {}

  private static class Parser {
    public static Card parseCard(String card) {
      String[] parts = card.trim().split(":");
      Long cardId = Long.parseLong(parts[0].split("\\s+")[1]);
      String[] lists = parts[1].split("\\|");
      List<Long> winningNumbers = Arrays.stream(lists[0].trim().split("\\s+")).map(Long::parseLong).toList();
      List<Long> playedNumbers = Arrays.stream(lists[1].trim().split("\\s+")).map(Long::parseLong).toList();
      return new Card(cardId, winningNumbers, playedNumbers);
    }
  }

  @Override
  public Object part1() {

    return dayStream().map(Parser::parseCard)
        .mapToLong(this::countWins)
        .map(count -> LongStream.rangeClosed(1, count).reduce((n, ignored) -> n * 2 ).orElse(0))
        .sum();
  }

  private long countWins(Card card) {
    return card.playedNumbers.stream().filter(card.winningNumbers::contains).count();
  }


  @Override
  public Object part2() {

    Map<Long, Pair<Card, Long>> cardsMap = new HashMap<>();

    dayStream().map(Parser::parseCard).forEach(card -> cardsMap.put(card.cardId, new Pair<>(card, 1L)));

    cardsMap.forEach((key, pair) -> {
      Long wins = countWins(pair.getLeft());
      Long increment = pair.getRight();
      LongStream.rangeClosed(key + 1, key + wins).forEach(id -> {
        Pair<Card, Long> actual = cardsMap.get(id);
        cardsMap.put(id, new Pair<>(actual.getLeft(), actual.getRight() + increment));
      });
    });

    return cardsMap.values().stream().mapToLong(Pair::getRight).sum();

  }
}
