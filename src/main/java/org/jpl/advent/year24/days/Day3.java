package org.jpl.advent.year24.days;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jpl.advent.year24.Day2024;

public class Day3 extends Day2024 {
  public Day3() {
    super(3);
  }

  public static void main(String[] args) {
    new Day3().printParts();
  }

  @Override
  public Object part1() {
    var matcher = getMatcherForInput("mul\\((\\d{1,3}),(\\d{1,3})\\)");
    var sum = 0;

    while (matcher.find()) {
      sum += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
    }

    return sum;
  }

  @Override
  public Object part2() {
    var matcher = getMatcherForInput("(do)\\(\\)|(don't)\\(\\)|(mul)\\((\\d{1,3}),(\\d{1,3})\\)");
    var enable = true;
    var sum = 0;

    while (matcher.find()) {
      if ("do".equals(matcher.group(1))) enable = true;
      if ("don't".equals(matcher.group(2))) enable = false;
      if ("mul".equals(matcher.group(3)) && enable) {
        sum += Integer.parseInt(matcher.group(4)) * Integer.parseInt(matcher.group(5));
      }
    }

    return sum;
  }

  private Matcher getMatcherForInput(String regex) {
    var pattern = Pattern.compile(regex, Pattern.MULTILINE);

    return pattern.matcher(day());
  }

}
