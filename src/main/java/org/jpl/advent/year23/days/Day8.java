package org.jpl.advent.year23.days;

import static java.util.function.Predicate.not;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.jpl.advent.year23.Day2023;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day8 extends Day2023 {
  public Day8() {
    super(8);
  }

  public static void main(String[] args) {
    new Day8().printParts();
  }

  private static final char LEFT = 'L';
  private static final char RIGHT = 'R';

  private static class Sequence {
    private final String movements;
    private int next;

    public Sequence(String movements) {
      this.movements = movements;
      reset();
    }

    public void reset() { next = 0; }

    public char nextMovement() {
      char movement = movements.charAt(next);
      next = (next + 1) % movements.length();
      return movement;
    }

  }

  @Builder
  @Getter
  public static class Node {
    private String name;
    private String left;
    private String right;
  }

  private Sequence sequence;
  private Map<String, Node> nodes;

  private void setup() {
    String[] parts = dayStrings("\n\n");
    sequence = new Sequence(parts[0]);
    nodes = Arrays.stream(parts[1].replaceAll("[=\\(\\)\\,]", "").split("\n"))
        .map(txt -> {
          String[] nodeParts = txt.split("\\s+");
          return Node.builder().name(nodeParts[0]).left(nodeParts[1]).right(nodeParts[2]).build();
        })
        .collect(Collectors.toMap(Node::getName, Function.identity()));
  }

  private int countSteps(Node node, Predicate<Node> keepLooking) {
    int steps = 0;
    Node actual = node;
    sequence.reset();
    while (keepLooking.test(actual)) {
      steps++;
      actual = switch (sequence.nextMovement()) {
        case LEFT -> nodes.get(actual.getLeft());
        case RIGHT -> nodes.get(actual.getRight());
        default -> throw new IllegalStateException("Unexpected value: " + sequence.nextMovement());
      };
    }
    return steps;
  }

  @Override
  public Object part1() {
    setup();
    Node startNode = nodes.get("AAA");
    return countSteps(startNode, node -> !"ZZZ".equals(node.getName()));
  }

  @Override
  public Object part2() {
    setup();
    List<Node> startList = nodes.values().stream().filter(n -> n.getName().endsWith("A")).toList();
    return startList.stream()
        .mapToLong( node -> countSteps(node, not(node1 -> node1.getName().endsWith("Z"))))
        .reduce(1, ArithmeticUtils::lcm);
  }

}
