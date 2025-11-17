package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.Util;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day24 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            ###########
            #0.1.....2#
            #.#######.#
            #4.......3#
            ###########
            """,
            "14"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return solve(input, false);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return solve(input, true);
    }

    private String solve(String input, boolean returnToStart) {
        var grid = Util.inputToGrid(input, Set.of('#'));
        var targets = new HashMap<Integer, Node>();

        for (var entry: grid.entrySet()) {
            char c = entry.getValue();

            if (Character.isDigit(c)) {
                int number = c - '0';
                targets.put(number, new Node(number, entry.getKey()));
            }
        }

        // Now first we would make a graph of all the targets and their distances to each other, that's how we would
        // calculate the shortest path in a much smaller graph
        findShortestDistancesBetweenTargets(grid, targets);

        // Now we would do a BFS on the graph consisting only of the targets (which is very small) and cover all paths
        var start = targets.get(0);
        var queue = new LinkedList<Path>() {{ add(new Path(start, 0, Set.of(0))); }};
        var shortestPath = Integer.MAX_VALUE;

        while (!queue.isEmpty()) {
            var path = queue.poll();

            if (path.visited.size() == targets.size()) {
                var steps = returnToStart ? path.steps + path.node.stepToNode.get(0) : path.steps;
                shortestPath = Math.min(shortestPath, steps);
                continue;
            }

            for (var node: path.node.nodes.values()) {
                if (path.visited.contains(node.number)) {
                    continue;
                }

                var newVisited = new HashSet<>(path.visited);
                newVisited.add(node.number);

                queue.add(new Path(node, path.steps + path.node.stepToNode.get(node.number), newVisited));
            }
        }

        return String.valueOf(shortestPath);
    }

    private void findShortestDistancesBetweenTargets(HashMap<Point, Character> grid, HashMap<Integer, Node> targets) {
        int relatedCount = targets.size() - 1;

        for (var target: targets.values()) {
            var visited = new HashSet<Point>();
            var queue = new LinkedList<PointStep>() {{ add(new PointStep(target.point, 0)); }};

            while (!queue.isEmpty() && target.stepToNode.size() < relatedCount) {
                var step = queue.poll();
                var point = step.point;
                var steps = step.steps;

                if (visited.contains(point)) {
                    continue;
                }

                visited.add(point);

                for (var adjacentPoint: point.getAdjacentPoints()) {
                    if (!visited.contains(adjacentPoint) && grid.containsKey(adjacentPoint)) {
                        char c = grid.get(adjacentPoint);

                        if (Character.isDigit(c)) {
                            var number = c - '0';

                            if (!target.nodes.containsKey(number)) {
                                target.nodes.put(number, targets.get(number));
                                target.stepToNode.put(number, steps + 1);
                            }
                        }

                        queue.add(new PointStep(adjacentPoint, steps + 1));
                    }
                }
            }
        }
    }

    record PointStep(Point point, int steps) {}
    record Path(Node node, int steps, Set<Integer> visited) {}

    private static class Node {
        public int number;
        public Point point;
        public Map<Integer, Integer> stepToNode = new HashMap<>();
        public Map<Integer, Node> nodes = new HashMap<>();

        public Node(int number, Point point) {
            this.number = number;
            this.point = point;
        }
    }
}
