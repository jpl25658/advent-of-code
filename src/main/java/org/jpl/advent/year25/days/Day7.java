package org.jpl.advent.year25.days;

import org.jpl.advent.common.Coord;
import org.jpl.advent.common.Grid;
import org.jpl.advent.year25.Day2025;
import java.util.*;

public class Day7 extends Day2025 {

    public Day7() {
        super(7);
    }

    public static void main(String[] args) {
        new Day7().printParts();
    }

    @Override
    public Object part1() {
        Grid grid = new Grid(dayGrid());
        Coord start = findStart(grid);

        return countBeams(grid, start);
    }

    @Override
    public Object part2() {
        Grid grid = new Grid(dayGrid());
        Coord start = findStart(grid);

        return countTimelines(grid, start, new HashMap<>());
    }

    private Coord findStart(Grid grid) {
        for (var col = 0; col < grid.colLength(); col++) {
            if (grid.get(0, col) == 'S') {
                return new Coord(0, col);
            }
        }
        throw new IllegalStateException("Start position not found in first row");
    }

    private int countBeams(Grid grid, Coord start) {
        Set<Coord> activeBeams = new HashSet<>();
        activeBeams.add(start);

        int splitCount = 0;
        for (int row = 1; row < grid.rowLength(); row++) {
            Set<Coord> newBeams = new HashSet<>();
            for (Coord beam : activeBeams) {
                Coord newCoord = new Coord(row, beam.col());
                if (grid.get(newCoord) == '.') {
                    newBeams.add(newCoord);
                } else if (grid.get(newCoord) == '^') {
                    splitCount++;
                    if (newCoord.col() - 1 >= 0) {
                        newBeams.add(new Coord(row, newCoord.col() - 1));
                    }
                    if (newCoord.col() + 1 < grid.colLength()) {
                        newBeams.add(new Coord(row, newCoord.col() + 1)); // right beam
                    }
                }
            }
            activeBeams = newBeams;
        }
        return splitCount;
    }

    private long countTimelines(Grid grid, Coord current, Map<Coord, Long> visited) {
        if (visited.containsKey(current)) {
            return visited.get(current);
        }

        if (current.row() >= grid.rowLength()) {
            return 1;
        }
        if (!grid.isValid(current)) {
            return 0;
        }

        long result;
        if (grid.get(current) == '^') {
            Coord left = new Coord(current.row(), current.col() - 1);
            Coord right = new Coord(current.row(), current.col() + 1);
            result = countTimelines(grid, left, visited) + countTimelines(grid, right, visited);
        } else {
            Coord nextPos = new Coord(current.row() + 1, current.col());
            result = countTimelines(grid, nextPos, visited);
        }
        visited.put(current, result);
        return result;
    }

}
