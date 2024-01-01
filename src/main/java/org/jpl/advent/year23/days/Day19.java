package org.jpl.advent.year23.days;

import static java.lang.Math.max;
import static java.lang.Math.min;
import org.jpl.advent.common.Pair;
import org.jpl.advent.year23.Day2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19 extends Day2023 {

  public Day19() {
    super(19);
  }

  public static void main(String[] args) {
    new Day19().printParts();
  }

  @Override
  public Object part1() {
    String[] data = dayStrings("\n\n");
    var workflows = readWorkflows(data[0]);
    var parts = readParts(data[1]);

    return parts.stream()
        .filter(part -> isPartAccepted(workflows, part))
        .mapToInt(part -> part.x() + part.m() + part.a() + part.s())
        .sum();
  }

  Map<String, Workflow> readWorkflows(String data) {
    return Arrays.stream(data.split("\n"))
        .map(workflow -> {
          final String regex = "(.+)\\{(.*)\\}";
          final Matcher matcher = Pattern.compile(regex).matcher(workflow);
          matcher.find();
          return new Workflow(
              matcher.group(1),
              readRules(matcher.group(2))
          );
        })
        .collect(Collectors.toMap(Workflow::name, Function.identity()));
  }

  List<Rule> readRules(String data) {
    return Arrays.stream(data.split(","))
        .map(rule -> {
          final String regex = "([xmas])([><])(\\d+):(.+)";
          final Matcher matcher = Pattern.compile(regex).matcher(rule);
          if (matcher.find()) {
            return new Rule(matcher.group(1),matcher.group(2), Integer.parseInt(matcher.group(3)), matcher.group(4));
          }
          return new Rule("x", ">", Integer.MIN_VALUE, rule); // always true
        })
        .toList();
  }


  List<Part> readParts(String data) {
    return Arrays.stream(data.split("\n"))
        .map(part -> {
          final String regex = "\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)\\}";
          final Matcher matcher = Pattern.compile(regex).matcher(part);
          matcher.find();
          return new Part(
              Integer.parseInt(matcher.group(1)),
              Integer.parseInt(matcher.group(2)),
              Integer.parseInt(matcher.group(3)),
              Integer.parseInt(matcher.group(4))
          );
        })
        .toList();
  }

  public static final String ACCEPTED = "A";
  public static final String REJECTED = "R";
  private static final List<String> FINAL_STATES = List.of(ACCEPTED, REJECTED);

  private record Workflow(String name, List<Rule> rules) { }
  private record Rule(String var, String op, Integer value, String dest) {
    public boolean isDirectJump() {
      return op == null;
    }
    public boolean isDestAccepted() {
      return ACCEPTED.equals(dest);
    }
    public boolean isDestRejected() {
      return REJECTED.equals(dest);
    }
    public String apply(Part part, String actualWorkflow) {
      if (isDirectJump()) return dest;
      int varValue = part.get(var);
      return (">".equals(op) && varValue > value) || ("<".equals(op) && varValue < value) ? dest : actualWorkflow;
    }
  }

  private record Part(int x, int m, int a, int s) {
    public int get(String letter) {
      return "x".equals(letter) ? x : "m".equals(letter) ? m : "a".equals(letter) ? a : s;
    }
  }

  private boolean isPartAccepted(Map<String, Workflow> workflows, Part part) {
    String actualWorkflow = "in";
    while (! FINAL_STATES.contains(actualWorkflow)) {
      Workflow current = workflows.get(actualWorkflow);
      for (var rule : current.rules()) {
        String nextWorkflow = rule.apply(part, actualWorkflow);
        if (! nextWorkflow.equals(actualWorkflow)) {
          actualWorkflow = nextWorkflow;
          break;
        }
      }
    }
    return ACCEPTED.equals(actualWorkflow);
  }

  @Override
  public Object part2() {
    var workflows = readWorkflows(dayStrings("\n\n")[0]);
    var acceptedRanges = new ArrayList<PartRange>();

    processRanges(PartRange.all(), workflows.get("in"), workflows, acceptedRanges);

    return acceptedRanges.stream()
        .mapToLong(PartRange::calcCombinations)
        .sum();
  }

  private void processRanges(PartRange range, Workflow wf, Map<String, Workflow> wfMap, List<PartRange> accepted) {
    PartRange actual = range;
    for (var rule : wf.rules()) {
      if (rule.isDirectJump()) {
        checkRuleDest(actual, wfMap, accepted, rule);
      } else {
        var splitRanges = actual.splitByRule(rule);
        checkRuleDest(splitRanges.a(), wfMap, accepted, rule);
        actual = splitRanges.b();
      }
    }
  }

  private void checkRuleDest(PartRange range, Map<String, Workflow> wfMap, List<PartRange> accepted, Rule rule) {
    if (rule.isDestAccepted()) {
      accepted.add(range);
    } else {
      if (! rule.isDestRejected()) {
        processRanges(range, wfMap.get(rule.dest()), wfMap, accepted);
      }
    }
  }

  private record PartRange(Pair<Long, Long> x, Pair<Long, Long> m, Pair<Long, Long> a, Pair<Long, Long> s) {
    private static final long MIN_RANGE = 1L;
    private static final long MAX_RANGE = 4000L;
    private static PartRange all() {
      var allValues = new Pair<>(MIN_RANGE, MAX_RANGE);
      return new PartRange(allValues, allValues, allValues, allValues);
    }
    private Pair<PartRange, PartRange> splitByRule(Rule rule) {
      return switch (rule.var()) {
        case "x" -> splitX(rule.op(), rule.value());
        case "m" -> splitM(rule.op(), rule.value());
        case "a" -> splitA(rule.op(), rule.value());
        case "s" -> splitS(rule.op(), rule.value());
        default -> throw new IllegalStateException("Unexpected value: " + rule.var());
      };
    }

    private Pair<PartRange, PartRange> splitX(String op, long value) {
      var valid = getValidRange(op, value, x);
      var invalid = getInvalidRange(op, value, x);
      return new Pair<>(new PartRange(valid, m, a, s), new PartRange(invalid, m, a, s));
    }

    private Pair<PartRange, PartRange> splitM(String op, long value) {
      var valid = getValidRange(op, value, m);
      var invalid = getInvalidRange(op, value, m);
      return new Pair<>(new PartRange(x, valid, a, s), new PartRange(x, invalid, a, s));
    }
    private Pair<PartRange, PartRange> splitA(String op, long value) {
      var valid = getValidRange(op, value, a);
      var invalid = getInvalidRange(op, value, a);
      return new Pair<>(new PartRange(x, m, valid, s), new PartRange(x, m, invalid, s));
    }

    private Pair<PartRange, PartRange> splitS(String op, long value) {
      var valid = getValidRange(op, value, s);
      var invalid = getInvalidRange(op, value, s);
      return new Pair<>(new PartRange(x, m, a, valid), new PartRange(x, m, a, invalid));
    }


    private static Pair<Long, Long> getValidRange(String op, long value, Pair<Long, Long> actual) {
      return "<".equals(op)
          ? new Pair<>(max(actual.a(), MIN_RANGE), min(actual.b(), value - 1))
          : new Pair<>(max(actual.a(), value + 1), min(actual.b(), MAX_RANGE));
    }

    private static Pair<Long, Long> getInvalidRange(String op, long value, Pair<Long, Long> actual) {
      return "<".equals(op)
          ? new Pair<>(max(actual.a(), value), min(actual.b(), MAX_RANGE))
          : new Pair<>(max(actual.a(), MIN_RANGE), min(actual.b(), value));
    }

    public long calcCombinations() {
      return (x.b() - x.a() + 1) * (m.b() - m.a() + 1) * (a.b() - a.a() + 1) * (s.b() - s.a() + 1);
    }

  }

}
