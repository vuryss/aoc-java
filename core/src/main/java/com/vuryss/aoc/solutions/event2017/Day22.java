package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.*;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day22 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            ..#
            #..
            ...
            """,
            "5587"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var nodes = new HashMap<Point, NodeType>();
        int maxX = 0, maxY = 0;

        for (var y = 0; y < lines.length; y++) {
            for (var x = 0; x < lines[y].length(); x++) {
                if (lines[y].charAt(x) == '#') {
                    nodes.put(new Point(x, y), NodeType.INFECTED);
                }
                maxX = x;
            }
            maxY = y;
        }

        var carrier = new Point(maxX / 2, maxY / 2);
        var direction = Direction.U;
        var infections = 0;

        for (var i = 0; i < 10000; i++) {
            var node = nodes.getOrDefault(carrier, NodeType.CLEAN);

            if (node == NodeType.CLEAN) {
                direction = direction.turnLeft();
                nodes.put(carrier, NodeType.INFECTED);
                infections++;
            } else {
                direction = direction.turnRight();
                nodes.remove(carrier);
            }

            carrier = carrier.goInDirection(direction);
        }

        return String.valueOf(infections);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var nodes = new HashMap<Point, NodeType>();
        int maxX = 0, maxY = 0;

        for (var y = 0; y < lines.length; y++) {
            for (var x = 0; x < lines[y].length(); x++) {
                if (lines[y].charAt(x) == '#') {
                    nodes.put(new Point(x, y), NodeType.INFECTED);
                }
                maxX = x;
            }
            maxY = y;
        }

        var carrier = new Point(maxX / 2, maxY / 2);
        var direction = Direction.U;
        var infections = 0;

        for (var i = 0; i < 10000000; i++) {
            var node = nodes.getOrDefault(carrier, NodeType.CLEAN);

            switch (node) {
                case CLEAN -> {
                    direction = direction.turnLeft();
                    nodes.put(carrier, NodeType.WEAKENED);
                }
                case WEAKENED -> {
                    nodes.put(carrier, NodeType.INFECTED);
                    infections++;
                }
                case INFECTED -> {
                    direction = direction.turnRight();
                    nodes.put(carrier, NodeType.FLAGGED);
                }
                case FLAGGED -> {
                    direction = direction.opposite();
                    nodes.remove(carrier);
                }
            }

            carrier = carrier.goInDirection(direction);
        }

        return String.valueOf(infections);
    }

    enum NodeType { CLEAN, WEAKENED, INFECTED, FLAGGED }
}
