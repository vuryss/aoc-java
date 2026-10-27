package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import java.util.*;

@SuppressWarnings("unused")
public class Day9 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            2199943210
            3987894921
            9856789892
            8767896789
            9899965678
            """,
            "15"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            2199943210
            3987894921
            9856789892
            8767896789
            9899965678
            """,
            "1134"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var grid = buildGrid(input);
        var sum = 0;

        outer:
        for (var entry: grid.entrySet()) {
            for (var point: entry.getKey().getAdjacentPoints()) {
                if (grid.getOrDefault(point, 9) <= entry.getValue()) {
                    continue outer;
                }
            }

            sum += entry.getValue() + 1;
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var grid = buildGrid(input);
        var basinSizes = new PriorityQueue<Integer>(Comparator.reverseOrder());

        outer:
        while (true) {
            var newGrid = new HashMap<>(grid);

            for (var entry: grid.entrySet()) {
                if (entry.getValue() == 9) {
                    continue;
                }

                var point = entry.getKey();
                var count = 0;
                var queue = new LinkedList<Point>(){{ add(point); }};
                newGrid.remove(point);

                while (!queue.isEmpty()) {
                    count++;

                    for (var adjacentPoint: queue.pollFirst().getAdjacentPoints()) {
                        if (newGrid.containsKey(adjacentPoint) && newGrid.getOrDefault(adjacentPoint, 9) != 9) {
                            queue.add(adjacentPoint);
                            newGrid.remove(adjacentPoint);
                        }
                    }
                }

                grid = newGrid;
                basinSizes.add(count);
                continue outer;
            }

            break;
        }

        return String.valueOf(basinSizes.poll() * basinSizes.poll() * basinSizes.poll());
    }

    private Map<Point, Integer> buildGrid(String input) {
        var lines = input.split("\n");
        var grid = new HashMap<Point, Integer>();

        for (var y = 0; y < lines.length; y++) {
            var line = lines[y].toCharArray();

            for (var x = 0; x < line.length; x++) {
                grid.put(new Point(x, y), Character.getNumericValue(line[x]));
            }
        }

        return grid;
    }
}
