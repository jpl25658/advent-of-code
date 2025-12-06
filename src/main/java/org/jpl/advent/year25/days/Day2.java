package org.jpl.advent.year25.days;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.jpl.advent.year25.Day2025;

public class Day2 extends Day2025 {

    public Day2() {
        super(2);
    }

    public static void main(String[] args) {
        new Day2().printParts();
    }

    @Override
    public Object part1() {
        List<Range> ranges = parseInput();
        long sum = 0;

        for (Range range : ranges) {
            for (long id = range.start; id <= range.end; id++) {
                if (isInvalid(id)) {
                    sum += id;
                }
            }
        }
        return sum;
    }

    @Override
    public Object part2() {
        List<Range> ranges = parseInput();
        long sum = 0;

        for (Range range : ranges) {
            for (long id = range.start; id <= range.end; id++) {
                if (isInvalid2(id)) {
                    sum += id;
                }
            }
        }
        return sum;
    }

    private boolean isInvalid(long id) {
        String idStr = String.valueOf(id);
        int length = idStr.length();
        if (length % 2 != 0) {
            return false;
        }
        String left = idStr.substring(0, length / 2);
        String right = idStr.substring(length / 2);

        return left.equals(right);
    }

    private boolean isInvalid2(long id) {
        String idStr = String.valueOf(id);
        int len = idStr.length();

        for (int repeatLength = 1; repeatLength <= len / 2; repeatLength++) {
            if (len % repeatLength == 0) {
                String pattern = idStr.substring(0, repeatLength);
                boolean isInvalid = true;
                for (int i = repeatLength; i < len; i += repeatLength) {
                    if (!idStr.substring(i, i + repeatLength).equals(pattern)) {
                        isInvalid = false;
                        break;
                    }
                }
                if (isInvalid) {
                    return true;
                }
            }
        }

        return false;
    }

    private record Range(long start, long end) {
        public static Range fromString(String s) {
            var parts = Arrays.stream(s.split("-")).mapToLong(Long::parseLong).toArray();
            return new Range(parts[0], parts[1]);
        }
    }

    List<Range> parseInput() {
        return Arrays.stream(dayStream().collect(Collectors.joining("")).split(","))
                .map(Range::fromString)
                .toList();
    }

}
