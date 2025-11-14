package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Direction;
import com.vuryss.aoc.util.Point;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

@SuppressWarnings("unused")
public class Day17 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
            """,
            "102"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
            """,
            "94",
            """
            111111111111
            999999999991
            999999999991
            999999999991
            999999999991
            """,
            "71"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return String.valueOf(solve(input, 1, 3));
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return String.valueOf(solve(input, 4, 10));
    }

    private int solve(String input, int minStraight, int maxStraight) {
        var lines = input.trim().split("\n");
        var grid = new HashMap<Point, Node>();

        for (var y = 0; y < lines.length; y++) {
            var line = lines[y];

            for (var x = 0; x < line.length(); x++) {
                grid.put(new Point(x, y), new Node(line.charAt(x) - '0'));
            }
        }

        var target = new Point(lines[0].length() - 1, lines.length - 1);

        var queue = new PriorityQueue<Player>(Comparator.comparingInt(a -> a.heatLoss));
        queue.add(new Player(new Point(0, 0), Direction.R, 0, 0));
        queue.add(new Player(new Point(0, 0), Direction.D, 0, 0));

        while (!queue.isEmpty()) {
            var player = queue.poll();
            var node = grid.get(player.position);

            if (player.position.equals(target) && player.chainSteps >= minStraight) {
                return player.heatLoss;
            }

            if (node.visited.contains(Pair.of(player.direction, player.chainSteps))) {
                continue;
            }

            node.visited.add(Pair.of(player.direction, player.chainSteps));

            if (player.chainSteps < maxStraight) {
                var nextPoint = player.position.goInDirection(player.direction);
                var nextNode = grid.get(nextPoint);

                if (nextNode != null && !nextNode.visited.contains(Pair.of(player.direction, player.chainSteps + 1))) {
                    queue.add(new Player(nextPoint, player.direction, player.chainSteps + 1, player.heatLoss + nextNode.heatLoss));
                }
            }

            if (player.chainSteps >= minStraight) {
                var rightPoint = player.position.rightFromDirection(player.direction);
                var rightDirection = player.direction.turnRight();
                var rightNode = grid.get(rightPoint);

                if (rightNode != null && !rightNode.visited.contains(Pair.of(rightDirection, 1))) {
                    queue.add(new Player(rightPoint, rightDirection, 1, player.heatLoss + rightNode.heatLoss));
                }

                var leftPoint = player.position.leftFromDirection(player.direction);
                var leftDirection = player.direction.turnLeft();
                var leftNode = grid.get(leftPoint);

                if (leftNode != null && !leftNode.visited.contains(Pair.of(leftDirection, 1))) {
                    queue.add(new Player(leftPoint, leftDirection, 1, player.heatLoss + leftNode.heatLoss));
                }
            }
        }

        throw new RuntimeException("No solution found");
    }

    private record Player(Point position, Direction direction, int chainSteps, int heatLoss) {}

    private static class Node {
        public final int heatLoss;
        public HashSet<Pair<Direction, Integer>> visited = new HashSet<>();

        public Node(int heatLoss) {
            this.heatLoss = heatLoss;
        }
    }
}
