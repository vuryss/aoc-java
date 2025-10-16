package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;

import java.util.*;

@SuppressWarnings("unused")
public class Day3 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
            """,
            "4361"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
            """,
            "467835"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        long sum = 0L;
        Set<Point> symbol = new HashSet<>();

        for (var y = 0; y < lines.length; y++) {
            for (var x = 0; x < lines[y].length(); x++) {
                var charAt = lines[y].charAt(x);

                if (!Character.isDigit(charAt) && charAt != '.') {
                    symbol.add(new Point(x, y));
                }
            }
        }

        var surrounding = new HashSet<Point>();
        var digits = "";

        for (var y = 0; y < lines.length; y++) {
            for (var x = 0; x < lines[y].length(); x++) {
                var point = new Point(x, y);
                var charAt = lines[y].charAt(x);

                if (Character.isDigit(charAt)) {
                    surrounding.addAll(point.surroundingPoints());
                    digits = digits + charAt;
                } else if (!digits.isEmpty()) {
                    for (var s: surrounding) {
                        if (symbol.contains(s)) {
                            sum += Integer.parseInt(digits);
                            break;
                        }
                    }

                    surrounding.clear();
                    digits = "";
                }
            }

            if (!digits.isEmpty()) {
                for (var s: surrounding) {
                    if (symbol.contains(s)) {
                        sum += Integer.parseInt(digits);
                        break;
                    }
                }

                surrounding.clear();
                digits = "";
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        long sum = 0L;
        Map<Point, List<Integer>> starNumbers = new HashMap<>();

        for (var y = 0; y < lines.length; y++) {
            for (var x = 0; x < lines[y].length(); x++) {
                if (lines[y].charAt(x) == '*') {
                    starNumbers.put(new Point(x, y), new ArrayList<>());
                }
            }
        }

        var surrounding = new HashSet<Point>();
        var digits = "";

        for (var y = 0; y < lines.length; y++) {
            var line = lines[y];

            for (var x = 0; x < line.length(); x++) {
                var point = new Point(x, y);
                var charAt = line.charAt(x);

                if (Character.isDigit(charAt)) {
                    surrounding.addAll(point.surroundingPoints());
                    digits = digits + charAt;
                } else if (!digits.isEmpty()) {
                    for (var s: surrounding) {
                        if (starNumbers.containsKey(s)) {
                            starNumbers.get(s).add(Integer.parseInt(digits));
                        }
                    }

                    surrounding.clear();
                    digits = "";
                }
            }

            if (!digits.isEmpty()) {
                for (var s: surrounding) {
                    if (starNumbers.containsKey(s)) {
                        starNumbers.get(s).add(Integer.parseInt(digits));
                    }
                }

                surrounding.clear();
                digits = "";
            }
        }

        for (var star: starNumbers.entrySet()) {
            var numbers = star.getValue();

            if (numbers.size() == 2) {
                sum += (long) numbers.get(0) * numbers.get(1);
            }
        }

        return String.valueOf(sum);
    }
}
