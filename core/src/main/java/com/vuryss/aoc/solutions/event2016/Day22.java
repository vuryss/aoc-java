package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Combinatorics;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day22 implements SolutionInterface {
    private int maxX;
    private double averageSize;

    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            root@ebhq-gridcenter# df -h
            Filesystem            Size  Used  Avail  Use%
            /dev/grid/node-x0-y0   10T    8T     2T   80%
            /dev/grid/node-x0-y1   11T    6T     5T   54%
            /dev/grid/node-x0-y2   32T   28T     4T   87%
            /dev/grid/node-x1-y0    9T    7T     2T   77%
            /dev/grid/node-x1-y1    8T    0T     8T    0%
            /dev/grid/node-x1-y2   11T    7T     4T   63%
            /dev/grid/node-x2-y0   10T    6T     4T   60%
            /dev/grid/node-x2-y1    9T    8T     1T   88%
            /dev/grid/node-x2-y2    9T    6T     3T   66%
            """,
            "7"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var nodes = parseNodes(input);

        return String.valueOf(Combinatorics.permutations(nodes, 2).stream().filter(pair -> {
            var a = pair.get(0);
            var b = pair.get(1);

            return a.used > 0 && a.used <= b.avail;
        }).count());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var nodes = parseNodes(input);
        var grid = new HashMap<Point, Node>();
        Point zeroNode = null;
        var immovableNodes = new HashSet<Point>();

        for (var node: nodes) {
            grid.put(node.location, node);

            if (null == zeroNode && node.used == 0) {
                zeroNode = node.location;
            }

            if (node.used > averageSize) {
                immovableNodes.add(node.location);
            }
        }

        var startNode = zeroNode;
        var dataNode = new Point(maxX, 0);
        var queue = new LinkedList<State>() {{ add(new State(startNode, 0)); }};
        var visited = new HashSet<Point>();

        // Target node is one to the left of the data node, so it can move in the direction of 0,0
        var targetNode = dataNode.left();
        var totalSteps = 0;

        while (!queue.isEmpty()) {
            var state = queue.poll();

            if (state.position.equals(targetNode)) {
                totalSteps = state.steps;
                break;
            }

            if (visited.contains(state.position)) {
                continue;
            }

            visited.add(state.position);

            for (var adjacentPoint: state.position.getAdjacentPoints()) {
                if (
                    !immovableNodes.contains(adjacentPoint)
                    && grid.containsKey(adjacentPoint)
                    && !visited.contains(adjacentPoint)
                ) {
                    queue.add(new State(adjacentPoint, state.steps + 1));
                }
            }
        }

        // One step to move it first time to the left, then 5 steps to move it once more to the left until the end
        return String.valueOf(totalSteps + 1 + (maxX - 1) * 5);
    }

    private List<Node> parseNodes(String input) {
        var lines = input.trim().split("\n");
        var nodes = new ArrayList<Node>();
        var linesCount = lines.length;

        for (var i = 2; i < linesCount; i++) {
            var ints = StringUtil.ints(lines[i]);
            nodes.add(new Node(new Point(ints.get(0), ints.get(1)), ints.get(2), ints.get(3), ints.get(4)));
            if (ints.get(0) > maxX) maxX = ints.get(0);
            averageSize += ints.get(2);
        }

        averageSize /= linesCount - 2;

        return nodes;
    }

    private record Node(Point location, int size, int used, int avail) {}
    private record State(Point position, int steps) {}
}
