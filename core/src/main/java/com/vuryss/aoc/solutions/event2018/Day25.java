package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point4D;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day25 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
              0,0,0,0
              3,0,0,0
              0,3,0,0
              0,0,3,0
              0,0,0,3
              0,0,0,6
              9,0,0,0
             12,0,0,0
            """,
            "2",
            """
            -1,2,2,0
            0,0,2,-2
            0,0,0,-2
            -1,2,0,0
            -2,-2,-2,2
            3,0,2,-1
            -1,3,2,2
            -1,0,-1,0
            0,2,1,-2
            3,0,0,0
            """,
            "4",
            """
            1,-1,0,1
            2,0,-1,0
            3,2,-1,0
            0,0,3,1
            0,0,-1,-1
            2,3,-2,0
            -2,2,0,0
            2,-2,0,-1
            1,-1,0,-1
            3,2,0,2
            """,
            "3",
            """
            1,-1,-1,-2
            -2,-2,0,1
            0,2,1,3
            -2,3,-2,1
            0,2,3,-2
            -1,-1,1,-2
            0,-2,-1,0
            -2,2,3,-1
            1,2,2,0
            -1,-2,0,-2
            """,
            "8"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var points = input.trim().lines()
            .map(l -> l.trim().split(","))
            .map(nums -> new Point4D(
                Long.parseLong(nums[0]),
                Long.parseLong(nums[1]),
                Long.parseLong(nums[2]),
                Long.parseLong(nums[3])
            ))
            .toList();
        var n = points.size();
        var constellations = n;
        int[] parents = new int[constellations];
        Arrays.setAll(parents, i -> i);

        for (var i = 0; i < n - 1; i++) {
            for (var j = i + 1; j < n; j++) {
                if (points.get(i).manhattanDistance(points.get(j)) <= 3) {
                    var parent1 = parent(parents, i);
                    var parent2 = parent(parents, j);

                    if (parent1 != parent2) {
                        parents[parent2] = parent1;
                        constellations--;
                    }
                }
            }
        }

        return String.valueOf(constellations);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return "Merry Christmas!";
    }

    private int parent(int[] parents, int i) {
        if (parents[i] == i) return i;
        return parent(parents, parents[i]);
    }
}
