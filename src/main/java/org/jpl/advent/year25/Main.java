package org.jpl.advent.year25;

import org.jpl.advent.common.Day;

import java.lang.reflect.InvocationTargetException;

public class Main {
  public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    for (int day = 1; day <= 12; day++) {
      System.out.println("Day " + day + ":");
      Day instance = (Day) Class.forName("org.jpl.advent.year25.days.Day" + day).getDeclaredConstructor().newInstance();
      instance.printParts();
      System.out.println();
    }
  }
}
