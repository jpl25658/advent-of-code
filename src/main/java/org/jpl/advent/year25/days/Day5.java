package org.jpl.advent.year25.days;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import org.jpl.advent.year25.Day2025;

public class Day5 extends Day2025 {

    public Day5() {
        super(5);
    }

    public static void main(String[] args) {
        new Day5().printParts();
    }

    @Override
    public Object part1() {
        Inventory inventory = parseInventory(Arrays.asList(dayStrings()));

        Predicate<Long> isFreshIngredient =
                ingredient -> inventory.ranges().stream().anyMatch(range -> range.contains(ingredient));

        return inventory.ingredients().stream()
                .filter(isFreshIngredient)
                .count();
    }

    @Override
    public Object part2() {
        Inventory inventory = parseInventory(Arrays.asList(dayStrings()));

        List<Range> identity = new ArrayList<>();
        BiFunction<List<Range>, Range, List<Range>> accumulator = (list, range) -> {
            if (list.isEmpty()) {
                list.add(range);
            } else {
                Range last = list.get(list.size() - 1);
                if (last.overlaps(range) || last.adjacent(range)) {
                    list.set(list.size() - 1, last.join(range));
                } else {
                    list.add(range);
                }
            }
            return list;
        };
        BinaryOperator<List<Range>> combiner = (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };

        return inventory.ranges().stream()
                .sorted((r1, r2) -> r1.min().compareTo(r2.min()))
                .reduce(identity, accumulator, combiner)
                .stream()
                .mapToLong(Range::size)
                .sum();
    }

    private Inventory parseInventory(List<String> lines) {
        var ranges = lines.stream()
                .map(String::trim)
                .takeWhile(line -> !line.isBlank())
                .map(line -> line.split("-"))
                .map(parts ->new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1])))
                .toList();

        var ingredients = lines.stream()
                .map(String::trim)
                .dropWhile(line -> !line.isBlank())
                .skip(1)
                .map(Long::parseLong)
                .toList();

        return new Inventory(ranges, ingredients);
    }
    private record Inventory(List<Range> ranges, List<Long> ingredients) {}

    private record Range(Long min, Long max) {
        boolean contains(Long value) {
            return value >= min && value <= max;
        }
        Long size() {
            return max - min + 1;
        }
        boolean overlaps(Range other) {
            return this.min <= other.max && other.min <= this.max;
        }
        boolean adjacent(Range other) {
            return this.max + 1 == other.min || other.max + 1 == this.min;
        }
        Range join(Range other) {
            return new Range(Math.min(this.min, other.min), Math.max(this.max, other.max));
        }

    }

}
