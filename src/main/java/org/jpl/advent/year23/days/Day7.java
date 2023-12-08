package org.jpl.advent.year23.days;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jpl.advent.year23.Day2023;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day7 extends Day2023 {
  public Day7() {
    super(7);
  }

  public static void main(String[] args) {
    new Day7().printParts();
  }

  private static final String CARD_VALUES = "AKQJT98765432";
  private static final String CARD_VALUES_JOKER = "AKQT98765432J";
  private static final Character JOKER = 'J';

  private enum HandType implements Comparable<HandType> {
    HIGH_CARD(1),
    ONE_PAIR(2),
    TWO_PAIR(3),
    THREE_KIND(4),
    FULL_HOUSE(5),
    FOUR_KIND(6),
    FIVE_KIND(7);

    private int value;
    HandType(int value) {
      this.value = value;
    }
  }

  @RequiredArgsConstructor
  @Getter
  private static class Hand {
    private final String cards;
    private final Long bet;
    private final HandType type;

    public String getCard(int index) {
      return String.valueOf(cards.charAt(index));
    }
  }

  private static class HandEvaluator implements Comparator<Hand> {

    private final boolean useJokers;
    private final String values;

    public HandEvaluator(boolean useJokers) {
      this.useJokers = useJokers;
      values = useJokers ? CARD_VALUES_JOKER : CARD_VALUES;
    }

    private int getCardValue(String card) {
      return values.indexOf(card) + 1;
    }

    public HandType evaluate(String handCards) {
      Map<Character, Long> cardsMap = handCards.chars().mapToObj(c -> (char) c)
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
      if (useJokers) {
        replaceJokers(cardsMap);
      }
      List<Long> repeatedCards = cardsMap.values().stream().sorted(Comparator.reverseOrder()).toList();
      return switch (repeatedCards.size()) {
        case 1 -> HandType.FIVE_KIND;
        case 2 -> repeatedCards.get(0) == 4 ? HandType.FOUR_KIND : HandType.FULL_HOUSE;
        case 3 -> repeatedCards.get(0) == 3 ? HandType.THREE_KIND : HandType.TWO_PAIR;
        case 4 -> HandType.ONE_PAIR;
        default -> HandType.HIGH_CARD;
      };
    }

    private void replaceJokers(Map<Character, Long> cardsMap) {
      long jokers = cardsMap.getOrDefault(JOKER,-1L);
      if (jokers == -1L) return;
      cardsMap.entrySet().stream()
        .filter(entry -> ! JOKER.equals(entry.getKey()))
        .max(Comparator.comparingLong(Map.Entry::getValue))
        .ifPresent(entry -> {
          cardsMap.remove(JOKER);
          cardsMap.merge(entry.getKey(), jokers, Long::sum);
        });
    }

    @Override
    public int compare(Hand hand1, Hand hand2) {
      int result = hand1.getType().compareTo(hand2.getType());
      if (result == 0) {
        int i = 0;
        while ((result == 0) && (i < hand1.getCards().length())) {
          result = Integer.compare(getCardValue(hand2.getCard(i)), getCardValue(hand1.getCard(i)));
          i++;
        }
      }
      return result;
    }
  }

  private long calcSumOfBets(HandEvaluator evaluator) {
    List<Hand> hands = dayStream()
        .map(input -> {
          String[] parts = input.split("\\s+");
          return new Hand(parts[0], Long.parseLong(parts[1]), evaluator.evaluate(parts[0]));
        })
        .sorted(evaluator)
        .toList();
    return IntStream.range(0, hands.size()).mapToLong(n -> hands.get(n).getBet() * (n + 1)).sum();
  }

  @Override
  public Object part1() {
    return calcSumOfBets(new HandEvaluator(false));
  }

  @Override
  public Object part2() {
    return calcSumOfBets(new HandEvaluator(true));
  }

}
