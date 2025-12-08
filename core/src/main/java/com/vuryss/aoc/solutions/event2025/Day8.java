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
        var junctions = new ArrayList<Point3D>();
        var connections = isTest ? 10 : 1000;

        for (var line: lines) {
            var coordinates = StringUtil.sints(line);
            junctions.add(new Point3D(coordinates.get(0), coordinates.get(1), coordinates.get(2)));
        }

        var combinations = Combinatorics.combinations(junctions, 2);
        combinations.sort(Comparator.comparingDouble(pair -> pair.getFirst().euclideanDistance(pair.get(1))));

        var circuits = new ArrayList<Set<Point3D>>();

        for (var i = 0; i < connections; i++) {
            var junction1 = combinations.get(i).get(0);
            var junction2 = combinations.get(i).get(1);
            boolean found = false;
            Set<Point3D> foundCircuit = null;

            for (var circuit: circuits) {
                if (circuit.contains(junction1) || circuit.contains(junction2)) {
                    if (found) {
                        foundCircuit.addAll(circuit);
                        circuits.remove(circuit);
                        break;
                    }

                    circuit.add(junction1);
                    circuit.add(junction2);
                    found = true;
                    foundCircuit = circuit;
                }
            }

            if (!found) {
                circuits.add(new HashSet<>(List.of(junction1, junction2)));
            }
        }

        long product = 1;
        var largestCircuits = circuits.stream().sorted(Comparator.<Set>comparingInt(Set::size).reversed()).limit(3).toList();

        for (var circuit: largestCircuits) {
            product *= circuit.size();
        }

        return String.valueOf(product);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var junctions = new ArrayList<Point3D>();

        for (var line: lines) {
            var coordinates = StringUtil.sints(line);
            junctions.add(new Point3D(coordinates.get(0), coordinates.get(1), coordinates.get(2)));
        }

        var combinations = Combinatorics.combinations(junctions, 2);
        combinations.sort(Comparator.comparingDouble(pair -> pair.getFirst().euclideanDistance(pair.get(1))));

        var circuits = new ArrayList<Set<Point3D>>();

        for (var combination: combinations) {
            var junction1 = combination.get(0);
            var junction2 = combination.get(1);
            boolean found = false;
            Set<Point3D> foundCircuit = null;

            for (var circuit: circuits) {
                if (circuit.contains(junction1) || circuit.contains(junction2)) {
                    if (found) {
                        foundCircuit.addAll(circuit);
                        circuits.remove(circuit);
                        break;
                    }

                    circuit.add(junction1);
                    circuit.add(junction2);
                    found = true;
                    foundCircuit = circuit;
                }
            }

            if (!found) {
                circuits.add(new HashSet<>(List.of(junction1, junction2)));
            }

            if (circuits.size() == 1 && circuits.getFirst().size() == junctions.size()) {
                return junction1.x * junction2.x + "";
            }
        }

        return "-not found-";
    }
}
