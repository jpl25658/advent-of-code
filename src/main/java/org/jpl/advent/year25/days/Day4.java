package org.jpl.advent.year25.days;


import org.jpl.advent.common.Grid;
import org.jpl.advent.year25.Day2025;

public class Day4 extends Day2025 {

    public Day4() {
        super(4);
    }

    public static void main(String[] args) {
        new Day4().printParts();
    }

    @Override
    public Object part1() {
        Grid map = new Grid(dayGrid(), '.', '.');

        return countAccesible(map);
    }

    @Override
    public Object part2() {
        Grid map = new Grid(dayGrid(), '.', '.');

        int totalAccessible = 0;
        int accessible;
        do {
            accessible = countAccesible(map);
            if (accessible > 0) {
                totalAccessible += accessible;
                removeAccessible(map);
            }
        } while (accessible > 0);

        return totalAccessible;
    }

    private int countAccesible(Grid map) {
        var accessible = 0;
        for (var row = 0; row < map.rowLength(); row++) {
            for (var col = 0; col < map.colLength(); col++) {
                if (map.get(row, col) == '@') {
                    if (countAdjacent(map, row, col ) < 4) {
                        map.set(row, col, 'x');
                        accessible++;
                    }
                }
            }
        }
        return accessible;
    }

    private int countAdjacent(Grid map, int row, int col) {
        var adjacent = 0;
        for (var dr = -1; dr <= 1; dr++) {
            for (var dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                if (map.get(row + dr, col + dc) != '.') {
                    adjacent++;
                }
            }
        }
        return adjacent;
    }

    private void removeAccessible(Grid map) {
        for (var row = 0; row < map.rowLength(); row++) {
            for (var col = 0; col < map.colLength(); col++) {
                if (map.get(row, col) == 'x') {
                    map.set(row, col, '.');
                }
            }
        }
    }

}
