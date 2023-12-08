package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.MathUtil;
import com.vuryss.aoc.util.Regex;

import java.util.*;

@SuppressWarnings("unused")
public class Day8 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            RL
            
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
            """,
            "2",
            """
            LLR
            
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
            """,
            "6"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            LR
            
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
            """,
            "6"
        );
    }

    @Override
    public String part1Solution(String input) {
        var parts = input.trim().split("\n\n");
        var moves = parts[0].trim();
        var nodes = parseNodes(parts[1]);

        var current = nodes.get("AAA");
        var index = 0;

        while (!current.name.equals("ZZZ")) {
            var move = moves.charAt(index++ % moves.length());
            current = move == 'L' ? current.left : current.right;
        }

        return String.valueOf(index);
    }

    @Override
    public String part2Solution(String input) {
        var parts = input.trim().split("\n\n");
        var moves = parts[0].trim();
        var nodes = parseNodes(parts[1]);
        var ghosts = new LinkedList<Ghost>();

        for (var node: nodes.entrySet()) {
            if (node.getKey().endsWith("A")) {
                var ghostNode = new Ghost();
                ghostNode.position = node.getValue();
                ghosts.add(ghostNode);
            }
        }

        var index = 0L;
        var allOnZ = false;
        var ghostCycleInterval = new HashMap<Ghost, Long>();

        do {
            var move = moves.charAt((int) (index++ % moves.length()));
            allOnZ = true;

            for (var ghost: ghosts) {
                ghost.position = move == 'L' ? ghost.position.left : ghost.position.right;

                if (ghost.position.name.endsWith("Z")) {
                    if (ghost.lastMatchIndex != null) {
                        ghostCycleInterval.put(ghost, index - ghost.lastMatchIndex);
                    }

                    ghost.lastMatchIndex = index;
                } else {
                    allOnZ = false;
                }
            }

            if (ghostCycleInterval.size() == ghosts.size()) {
                return String.valueOf(MathUtil.lcm(ghostCycleInterval.values()));
            }
        } while (!allOnZ);

        return String.valueOf(index);
    }

    private HashMap<String, Node> parseNodes(String input) {
        var nodes = new HashMap<String, Node>();

        for (var map: input.trim().split("\n")) {
            var matches = Regex.matchAll("\\w+", map);
            var node = new Node(matches.getFirst(), matches.get(1), matches.get(2));
            nodes.put(node.name, node);
        }

        for (var nodeEntrySet: nodes.entrySet()) {
            var node = nodeEntrySet.getValue();
            node.left = nodes.get(node.leftName);
            node.right = nodes.get(node.rightName);
        }

        return nodes;
    }

    public static class Ghost {
        public Node position;
        public Long lastMatchIndex = null;
    }

    public static class Node {
        public String name;
        public String leftName;
        public String rightName;
        public Node left;
        public Node right;

        public Node(String name, String leftName, String rightName) {
            this.name = name;
            this.leftName = leftName;
            this.rightName = rightName;
        }
    }
}
