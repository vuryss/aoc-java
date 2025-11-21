package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

@SuppressWarnings("unused")
public class Day14 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of("flqrgnkx", "8108");
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        input = input.trim();
        var str = new StringBuilder();

        for (var i = 0; i < 128; i++) {
            str.append(new KnotHash(input + "-" + i).binaryForm());
        }

        return String.valueOf(str.toString().chars().filter(c -> c == '1').count());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        input = input.trim();
        var grid = new HashMap<Point, Boolean>();

        for (var i = 0; i < 128; i++) {
            var hash = new KnotHash(input + "-" + i).binaryForm();

            for (var j = 0; j < 128; j++) {
                grid.put(new Point(i, j), hash.charAt(j) == '1');
            }
        }

        var regions = 0;
        var visited = new HashSet<Point>();

        for (var es: grid.entrySet()) {
            if (!es.getValue() || visited.contains(es.getKey())) {
                continue;
            }

            visited.add(es.getKey());
            regions++;

            var queue = new LinkedList<Point>();
            queue.add(es.getKey());

            while (!queue.isEmpty()) {
                var point = queue.poll();

                for (var adjacentPoint: point.getAdjacentPoints()) {
                    if (grid.getOrDefault(adjacentPoint, false) && !visited.contains(adjacentPoint)) {
                        visited.add(adjacentPoint);
                        queue.add(adjacentPoint);
                    }
                }
            }
        }

        return String.valueOf(regions);
    }
}
