package org.jpl.advent.year25.days;


import org.jpl.advent.year25.Day2025;

public class Day1 extends Day2025 {

    public Day1() {
        super(1);
    }

    public static void main(String[] args) {
        new Day1().printParts();
    }

    @Override
    public Object part1() {
        int count = 0;

        int dial = 50;
        for (String line : dayStrings()) {
            int sign = line.charAt(0) == 'L' ? -1 : 1;
            int number = Integer.parseInt(line.substring(1));
            dial = (dial + (sign * number)) % 100;
            if (dial < 0) {
                dial += 100;
            }
            if (dial == 0) {
                count++;
            }
        }

        return count;
    }

    @Override
    public Object part2() {
        int count = 0;

        int dial = 50;
        for (String line : dayStrings()) {
            int sign = line.charAt(0) == 'L' ? -1 : 1;
            int number = Integer.parseInt(line.substring(1));
            while (number > 100) {
                number -= 100;
                count++;
            }
            boolean wasZero = (dial == 0);
            dial = (dial + (sign * number));
            if (dial == 0) {
                count++;
            }
            if (dial < 0) {
                dial += 100;
                if (!wasZero) {
                    count++;
                }
            }
            if (dial >= 100) {
                dial -= 100;
                count++;
            }
        }

        return count;
    }

}
