package org.jpl.advent.year25.days;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jpl.advent.year25.Day2025;

public class Day8 extends Day2025 {

    public Day8() {
        super(8);
    }

    public static void main(String[] args) {
        new Day8().printParts();
    }

    @Override
    public Object part1() {
        Point3D[] points = parsePoints();
        Junction[] junctions = findShortestJunctionDistances(points, example != 0 ? 10 : 1000);

        DSU circuits = new DSU(points.length);
        for (Junction junction : junctions) {
            if (circuits.isNotConnected(junction.from(), junction.to())) {
                circuits.union(junction.from(), junction.to());
            }
        }

        Map<Integer, Integer> circuitSizes = new HashMap<>();
        for (int i = 0; i < points.length; i++) {
            int root = circuits.find(i);
            circuitSizes.put(root, circuitSizes.getOrDefault(root, 0) + 1);
        }

        return circuitSizes.values().stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce(1, (a, b) -> a * b);
    }

    @Override
    public Object part2() {
        Point3D[] points = parsePoints();
        Junction[] junctions = findShortestJunctionDistances(points, Integer.MAX_VALUE);

        DSU circuits = new DSU(points.length);
        Junction lastJunction = null;

        for (Junction junction : junctions) {
            if (circuits.isNotConnected(junction.from(), junction.to())) {
                circuits.union(junction.from(), junction.to());
                lastJunction = junction;
                if (circuits.count() == 1) {
                    break;
                }
            }
        }

        assert lastJunction != null;
        long x1 = points[lastJunction.from()].x();
        long x2 = points[lastJunction.to()].x();
        return x1 * x2;
    }

    private Point3D[] parsePoints() {
        return dayStream()
                .map(line -> line.split(","))
                .map(parts -> new Point3D(Long.parseLong(parts[0].trim()), Long.parseLong(parts[1].trim()), Long.parseLong(parts[2].trim())))
                .toArray(Point3D[]::new);
    }

    private Junction[] findShortestJunctionDistances(Point3D[] points, int count) {
        List<Junction> junctions = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                junctions.add(new Junction(i, j, points[i].distance(points[j])));
            }
        }
        junctions.sort(Comparator.comparingLong(Junction::distance));

        return junctions.subList(0, Math.min(count, junctions.size())).toArray(Junction[]::new);
    }

    private record Point3D(long x, long y, long z) {
        public long distance(Point3D other) {
            return (this.x - other.x) * (this.x - other.x)
                    + (this.y - other.y) * (this.y - other.y)
                    + (this.z - other.z) * (this.z - other.z);
        }
    }

    private record Junction(int from, int to, long distance) {}

    // https://cp-algorithms.com/data_structures/disjoint_set_union.html
    private static class DSU {
        private final int[] parent;
        private final int[] rank;

        public DSU(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int point) {
            if (parent[point] != point) {
                parent[point] = find(parent[point]);
            }
            return parent[point];
        }

        public boolean isNotConnected(int pointA, int pointB) {
            return find(pointA) != find(pointB);
        }

        public void union(int pointA, int pointB) {
            int rootA = find(pointA);
            int rootB = find(pointB);

            if (rootA != rootB) {
                if (rank[rootA] < rank[rootB]) {
                    parent[rootA] = rootB;
                } else if (rank[rootA] > rank[rootB]) {
                    parent[rootB] = rootA;
                } else {
                    parent[rootB] = rootA;
                    rank[rootA]++;
                }
            }
        }

        public int count() {
                Set<Integer> roots = new HashSet<>();
                for(int i = 0; i < parent.length; i++) {
                    roots.add(find(i));
                }
                return roots.size();
        }

    }

}
