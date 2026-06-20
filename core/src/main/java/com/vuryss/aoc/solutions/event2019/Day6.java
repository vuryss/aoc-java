package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day6 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            COM)B
            B)C
            C)D
            D)E
            E)F
            B)G
            G)H
            D)I
            E)J
            J)K
            K)L
            """,
            "42"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            COM)B
            B)C
            C)D
            D)E
            E)F
            B)G
            G)H
            D)I
            E)J
            J)K
            K)L
            K)YOU
            I)SAN
            """,
            "4"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var nodes = new HashMap<String, Node>();
        var count = 0L;

        for (var line : input.trim().split("\n")) {
            var parts = line.trim().split("\\)");
            var node1 = nodes.getOrDefault(parts[0], new Node(parts[0]));
            var node2 = nodes.getOrDefault(parts[1], new Node(parts[1]));
            node2.orbits = node1;
            nodes.put(parts[0], node1);
            nodes.put(parts[1], node2);
        }

        for (var node : nodes.values()) {
            count += node.orbitCount();
        }

        return count + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var nodes = new HashMap<String, Node>();

        for (var line : input.trim().split("\n")) {
            var parts = line.trim().split("\\)");
            var node1 = nodes.getOrDefault(parts[0], new Node(parts[0]));
            var node2 = nodes.getOrDefault(parts[1], new Node(parts[1]));
            node2.orbits = node1;
            node2.linked.add(node1);
            node1.linked.add(node2);
            nodes.put(parts[0], node1);
            nodes.put(parts[1], node2);
        }

        var queue = new LinkedList<State>();
        var targetNode = nodes.get("SAN").orbits;
        var visited = new HashSet<Node>();
        visited.add(nodes.get("YOU"));
        visited.add(nodes.get("YOU").orbits);
        queue.add(new State(nodes.get("YOU").orbits, 0));

         while (!queue.isEmpty()) {
             var state = queue.poll();

             for (var linked: state.node.linked) {
                 if (linked.equals(targetNode)) return (state.steps + 1) + "";
                 if (visited.contains(linked)) continue;
                 visited.add(linked);
                 queue.add(new State(linked, state.steps + 1));
             }
         }

        return "-not-found-";
    }

    private record State(Node node, int steps) {}

    private static class Node {
        public String name;
        public Node orbits;
        public long orbitsCount = -1;
        public Set<Node> linked = new HashSet<>();

        public Node(String name) {
            this.name = name;
        }

        public long orbitCount() {
            if (orbitsCount < 0) orbitsCount = name.equals("COM") ? 0 : orbits.orbitCount() + 1;

            return orbitsCount;
        }
    }
}
