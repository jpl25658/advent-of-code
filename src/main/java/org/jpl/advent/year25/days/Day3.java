package org.jpl.advent.year25.days;


import org.jpl.advent.year25.Day2025;

public class Day3 extends Day2025 {

    public Day3() {
        super(3);
    }

    public static void main(String[] args) {
        new Day3().printParts();
    }

    @Override
    public Object part1() {
        String[] banks = dayStrings();
        long totalJoltage = 0;

        for (String bank : banks) {
            long maxJoltage = findMaxJoltage(bank);
            totalJoltage += maxJoltage;
        }

        return totalJoltage;
    }

    @Override
    public Object part2() {
        String[] banks = dayStrings();
        long totalJoltage = 0;

        for (String bank : banks) {
            long maxJoltage = findMaxJoltage12(bank);
            totalJoltage += maxJoltage;
        }

        return totalJoltage;
    }

    private int findMaxJoltage(String bank) {
        int max = 0;

        for (int i = 0; i < bank.length(); i++) {
            for (int j = i + 1; j < bank.length(); j++) {
                int digit1 = bank.charAt(i) - '0';
                int digit2 = bank.charAt(j) - '0';
                max = Math.max(max, digit1 * 10 + digit2);
            }
        }

        return max;
    }

    private long findMaxJoltage12(String bank) {
        int size = bank.length();
        int remove = size - 12;
        char[] batteries = bank.toCharArray();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < size; i++) {
            char digit = batteries[i];
            while (!result.isEmpty() && result.charAt(result.length() - 1) < digit && remove > 0) {
                result.deleteCharAt(result.length() - 1);
                remove--;
            }
            result.append(digit);
        }
        while (remove > 0) {
            result.deleteCharAt(result.length() - 1);
            remove--;
        }

        return Long.parseLong(result.toString());
    }

}
