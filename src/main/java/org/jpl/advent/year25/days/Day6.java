package org.jpl.advent.year25.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import org.jpl.advent.year25.Day2025;
import java.math.BigInteger;

public class Day6 extends Day2025 {

    public Day6() {
        super(6);
    }

    public static void main(String[] args) {
        new Day6().printParts();
    }

    @Override
    public Object part1() {
        List<Problem> problems = parseProblems1();

        BigInteger total = BigInteger.ZERO;
        for (Problem problem : problems) {
            BigInteger result = solveProblem(problem);
            total = total.add(result);
        }

        return total;
    }

    @Override
    public Object part2() {
        List<Problem> problems = parseProblems2();

        BigInteger total = BigInteger.ZERO;
        for (Problem problem : problems) {
            BigInteger result = solveProblem(problem);
            total = total.add(result);
        }

        return total;
    }

    private record Problem (List<BigInteger> numbers, String operator) {}

    private BigInteger solveProblem(Problem problem) {
        BigInteger result = problem.numbers.getFirst();

        for (int i = 1; i < problem.numbers.size(); i++) {
            if ("*".equals(problem.operator)) {
                result = result.multiply(problem.numbers.get(i));
            }
            if ("+".equals(problem.operator)) {
                result = result.add(problem.numbers.get(i));
            }
        }

        return result;
    }

    private List<Problem> parseProblems1() {
        String[] lines = dayStrings();
        String[] operators = lines[lines.length - 1].trim().split("\\s+");
        List<List<BigInteger>> numbersLists = parseProblems1Lines(lines, lines.length - 1);

        List<Problem> problems = new ArrayList<>();
        for (int i = 0; i < operators.length; i++) {
            List<BigInteger> numbers = new ArrayList<>();
            for (List<BigInteger> list : numbersLists) {
                numbers.add(list.get(i));
            }
            problems.add(new Problem(numbers, operators[i]));
        }

        return  problems;
    }

    private static List<List<BigInteger>> parseProblems1Lines(String[] lines, int numLines) {
        return IntStream.range(0, numLines)
                .mapToObj(i -> parseProblems1Numbers(lines[i]))
                .toList();
    }

    private static List<BigInteger> parseProblems1Numbers(String line) {
        return Arrays.stream(line.trim().split("\\s+"))
                .filter(s -> s.matches("\\d+"))
                .map(BigInteger::new)
                .toList();
    }

    private List<Problem> parseProblems2() {
        char[][] worksheet = dayGrid();
        char[] operators = worksheet[worksheet.length - 1];
        int rows = worksheet.length - 1;
        int cols = worksheet[0].length;

        List<Problem> problems = new ArrayList<>();
        List<BigInteger> numbers = new ArrayList<>();
        for (int col = cols - 1; col >= 0; col--) {
            StringBuilder builder = new StringBuilder();
            for (int row = 0; row < rows; row++) {
                builder.append(worksheet[row][col]);
            }
            var str = builder.toString().trim();
            if (!str.isBlank()) {
                numbers.add(new BigInteger(str));
            }
            if (operators[col] != ' ') {
                problems.add(new Problem(numbers, String.valueOf(operators[col])));
                numbers = new ArrayList<>();
            }
        }

        return  problems;
    }

}
