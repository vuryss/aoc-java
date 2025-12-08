package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Combinatorics;
import com.vuryss.aoc.util.Point3D;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day8 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            162,817,812
            57,618,57
            906,360,560
            592,479,940
            352,342,300
            466,668,158
            542,29,236
            431,825,988
            739,650,466
            52,470,668
            216,146,977
            819,987,18
            117,168,530
            805,96,715
            346,949,466
            970,615,88
            941,993,340
            862,61,35
            984,92,344
            425,690,689
            """,
            "40"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            162,817,812
            57,618,57
            906,360,560
            592,479,940
            352,342,300
            466,668,158
            542,29,236
            431,825,988
            739,650,466
            52,470,668
            216,146,977
            819,987,18
            117,168,530
            805,96,715
            346,949,466
            970,615,88
            941,993,340
            862,61,35
            984,92,344
            425,690,689
            """,
            "25272"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var junctions = new ArrayList<JunctionBox>();
        var connections = isTest ? 10 : 1000;

        for (var line: lines) {
            var coordinates = StringUtil.ints(line);
            junctions.add(new JunctionBox(new Point3D(coordinates.get(0), coordinates.get(1), coordinates.get(2))));
        }

        var combinations = Combinatorics.combinations(junctions, 2);
        combinations.sort(Comparator.comparingDouble(
            pair -> pair.getFirst().position.euclideanDistance(pair.get(1).position)
        ));

        for (var i = 0; i < connections; i++) {
            var junction1 = combinations.get(i).get(0);
            var junction2 = combinations.get(i).get(1);

            if (junction1.circuit != junction2.circuit) {
                junction1.circuit.addAll(junction2.circuit);

                for (var junction: junction2.circuit) {
                    junction.circuit = junction1.circuit;
                }
            }
        }

        return junctions.stream()
            .map(j -> j.circuit)
            .distinct()
            .map(Set::size)
            .sorted(Comparator.reverseOrder())
            .limit(3)
            .reduce(1, (a, b) -> a * b) + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var junctions = new ArrayList<JunctionBox>();

        for (var line: lines) {
            var coordinates = StringUtil.sints(line);
            junctions.add(new JunctionBox(new Point3D(coordinates.get(0), coordinates.get(1), coordinates.get(2))));
        }

        var combinations = Combinatorics.combinations(junctions, 2);
        combinations.sort(Comparator.comparingDouble(
            pair -> pair.getFirst().position.euclideanDistance(pair.get(1).position)
        ));

        for (var combination: combinations) {
            var junction1 = combination.get(0);
            var junction2 = combination.get(1);

            if (junction1.circuit != junction2.circuit) {
                junction1.circuit.addAll(junction2.circuit);

                for (var junction: junction2.circuit) {
                    junction.circuit = junction1.circuit;
                }
            }

            if (junction1.circuit.size() == junctions.size()) {
                return junction1.position.x * junction2.position.x + "";
            }
        }

        return "-not found-";
    }

    private static class JunctionBox {
        public Point3D position;
        public Set<JunctionBox> circuit;

        public JunctionBox(Point3D position) {
            this.position = position;
            this.circuit = new HashSet<>();
            this.circuit.add(this);
        }
    }
}
