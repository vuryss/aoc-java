package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.Util;

import java.util.*;

@SuppressWarnings("unused")
public class Day23 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            #.#####################
            #.......#########...###
            #######.#########.#.###
            ###.....#.>.>.###.#.###
            ###v#####.#v#.###.#.###
            ###.>...#.#.#.....#...#
            ###v###.#.#.#########.#
            ###...#.#.#.......#...#
            #####.#.#.#######.#.###
            #.....#.#.#.......#...#
            #.#####.#.#.#########v#
            #.#...#...#...###...>.#
            #.#.#v#######v###.###v#
            #...#.>.#...>.>.#.###.#
            #####v#.#.###v#.#.###.#
            #.....#...#...#.#.#...#
            #.#########.###.#.#.###
            #...###...#...#...#.###
            ###.###.#.###v#####v###
            #...#...#.#.>.>.#.>.###
            #.###.###.#.###.#.#v###
            #.....###...###...#...#
            #####################.#
            """,
            "94"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            #.#####################
            #.......#########...###
            #######.#########.#.###
            ###.....#.>.>.###.#.###
            ###v#####.#v#.###.#.###
            ###.>...#.#.#.....#...#
            ###v###.#.#.#########.#
            ###...#.#.#.......#...#
            #####.#.#.#######.#.###
            #.....#.#.#.......#...#
            #.#####.#.#.#########v#
            #.#...#...#...###...>.#
            #.#.#v#######v###.###v#
            #...#.>.#...>.>.#.###.#
            #####v#.#.###v#.#.###.#
            #.....#...#...#.#.#...#
            #.#########.###.#.#.###
            #...###...#...#...#.###
            ###.###.#.###v#####v###
            #...#...#.#.>.>.#.>.###
            #.###.###.#.###.#.#v###
            #.....###...###...#...#
            #####################.#
            """,
            "154"
        );
    }

    @Override
    public String part1Solution(String input) {
        var grid = Util.inputToGrid(input, Set.of('#'));
        var startPoint = grid.keySet().stream().min(Comparator.comparingInt(p -> p.y)).orElseThrow();
        var endPoint = grid.keySet().stream().max(Comparator.comparingInt(p -> p.y)).orElseThrow();
        var queue = new LinkedList<>(List.of(new StepPlayer(startPoint, 0, new HashSet<>())));
        var maxSteps = 0;

        while (!queue.isEmpty()) {
            var player = queue.poll();

            if (player.point.equals(endPoint)) {
                maxSteps = Math.max(maxSteps, player.steps);
                continue;
            }

            player.visited.add(player.point);

            (switch (grid.get(player.point)) {
                case '>' -> List.of(player.point.right());
                case '<' -> List.of(player.point.left());
                case 'v' -> List.of(player.point.down());
                case '^' -> List.of(player.point.up());
                default -> player.point.getAdjacentPoints();
            })  .stream()
                .filter(grid::containsKey)
                .filter(nextPoint -> !player.visited.contains(nextPoint))
                .forEach(nextPoint -> queue.add(new StepPlayer(nextPoint, player.steps + 1, new HashSet<>(player.visited))));
        }

        return String.valueOf(maxSteps);
    }

    @Override
    public String part2Solution(String input) {
        var grid = Util.inputToGrid(input, Set.of('#'));
        var startPoint = grid.keySet().stream().min(Comparator.comparingInt(p -> p.y)).orElseThrow();
        var endPoint = grid.keySet().stream().max(Comparator.comparingInt(p -> p.y)).orElseThrow();
        var junctions = new HashMap<>(Map.of(startPoint, new Node(startPoint), endPoint, new Node(endPoint)));

        // Construct all nodes, skipping straight paths and accounting only for junctions (plus start and end points)
        for (var es: grid.entrySet()) {
            var countAdjacent = es.getKey().getAdjacentPoints().stream().filter(grid::containsKey).count();

            if (countAdjacent > 2) {
                junctions.put(es.getKey(), new Node(es.getKey()));
            }
        }

        // Construct all edges between nodes
        for (var point: junctions.keySet()) {
            var visited = new HashSet<>(Set.of(point));
            var queue = new LinkedList<>(List.of(new StepPlayer(point, 0, visited)));

            while (!queue.isEmpty()) {
                var player = queue.poll();
                player.visited.add(player.point);

                for (var nextPoint: player.point.getAdjacentPoints()) {
                    if (!grid.containsKey(nextPoint) || player.visited.contains(nextPoint)) {
                        continue;
                    }

                    if (junctions.containsKey(nextPoint)) {
                        var prevNode = junctions.get(point);
                        var nextNode = junctions.get(nextPoint);

                        prevNode.nodes.put(nextNode, player.steps + 1);
                        nextNode.nodes.put(prevNode, player.steps + 1);

                        continue;
                    }

                    queue.add(new StepPlayer(nextPoint, player.steps + 1, new HashSet<>(player.visited)));
                }
            }
        }

        var maxSteps = 0;
        var queue = new LinkedList<>(List.of(new NodePlayer(junctions.get(startPoint), 0, new HashSet<>())));

        while (!queue.isEmpty()) {
            var player = queue.removeLast();

            if (player.node.point.equals(endPoint)) {
                maxSteps = Math.max(maxSteps, player.steps);
                continue;
            }

            player.visited.add(player.node);

            for (var es: player.node.nodes.entrySet()) {
                if (!player.visited.contains(es.getKey())) {
                    queue.add(new NodePlayer(es.getKey(), player.steps + es.getValue(), new HashSet<>(player.visited)));
                }
            }
        }

        return String.valueOf(maxSteps);
    }

    private static class StepPlayer {
        public Point point;
        public int steps;
        public HashSet<Point> visited;

        public StepPlayer(Point point, int steps, HashSet<Point> visited) {
            this.point = point;
            this.steps = steps;
            this.visited = visited;
        }
    }

    private static class NodePlayer {
        public Node node;
        public int steps;
        public HashSet<Node> visited;

        public NodePlayer(Node node, int steps, HashSet<Node> visited) {
            this.node = node;
            this.steps = steps;
            this.visited = visited;
        }
    }

    private static class Node {
        public Point point;
        public Map<Node, Integer> nodes = new HashMap<>();

        public Node(Point point) {
            this.point = point;
        }
    }
}