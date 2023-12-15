package org.jpl.advent.year23.days;

import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;
import org.jpl.advent.year23.Day2023;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Day15 extends Day2023 {
  public Day15() {
    super(15);
  }

  public static void main(String[] args) {
    new Day15().printParts();
  }

  String[] data;
  Map<Integer, Map<String, Integer>> boxes = new LinkedHashMap<>();

  @Override
  public Object part1() {
    data = day().replaceAll("\\n", "").split(",");
    return stream(data).mapToInt(this::hashCode).sum();
  }

  @Override
  public Object part2() {
    data = day().replaceAll("\\n", "").split(",");

    rangeClosed(0, 255).forEach(n -> boxes.put(n, new LinkedHashMap<>()));
    range(0, data.length).forEach(this::processLabel);

    return rangeClosed(0, 255).map(this::focusingPower).sum();
  }

  private int hashCode(String txt) {
    int n = 0;
    for (int i = 0; i < txt.length(); i++) {
      n = (n + txt.charAt(i)) * 17 % 256;
    }
    return n;
  }

  private void processLabel(int n) {
    String[] parts = data[n].split("[=\\-]");
    String lbl = parts[0];
    int box = hashCode(lbl);
    if (parts.length == 1) {
      boxes.get(box).remove(lbl);
    } else {
      boxes.get(box).put(lbl, Integer.parseInt(parts[1]));
    }
  }

  private int focusingPower(int nBox) {
    List<Integer> lenses = boxes.get(nBox).values().stream().toList();
    return range(0, lenses.size()).map(n -> (nBox + 1) * (n + 1) * lenses.get(n)).sum();
  }

}
