package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.StringUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day22 implements SolutionInterface {
    private int depth;
    private Point target;
    private Map<Point, Integer> erosionLevels;

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            depth: 510
            target: 10,10
            """,
            "114"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            depth: 510
            target: 10,10
            """,
            "45"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var inputData = StringUtil.ints(input);
        var riskLevel = 0;

        depth = inputData.getFirst();
        target = new Point(inputData.get(1), inputData.get(2));
        erosionLevels = new HashMap<>();

        for (int x = 0; x <= target.x; x++) {
            for (int y = 0; y <= target.y; y++) {
                riskLevel += RegionType.ofErosionLevel(erosionLevel(new Point(x, y))).riskLevel;
            }
        }

        return String.valueOf(riskLevel);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var inputData = StringUtil.ints(input);
        int LEEWAY = 50;

        depth = inputData.getFirst();
        target = new Point(inputData.get(1), inputData.get(2));
        erosionLevels = new HashMap<>();
        Map<Point, RegionType> pointType = new HashMap<>();

        for (int x = 0; x <= target.x + LEEWAY; x++) {
            for (int y = 0; y <= target.y + LEEWAY; y++) {
                var point = new Point(x, y);

                pointType.put(point, RegionType.ofErosionLevel(erosionLevel(point)));
            }
        }

        var queue = new PriorityQueue<State>(Comparator.comparingInt(s -> s.minutes));
        var visitedMinutes = new HashMap<Pair<Point, Tool>, Integer>();
        var minPath = Integer.MAX_VALUE;

        queue.add(new State(Tool.TORCH, new Point(0, 0), 0));

        while (!queue.isEmpty()) {
            var state = queue.poll();
            var allowedTools = pointType.get(state.point).allowedTools;

            for (var adjPoint : state.point.getAdjacentPoints()) {
                if (adjPoint.x < 0 || adjPoint.x > target.x + LEEWAY || adjPoint.y < 0 || adjPoint.y > target.y + LEEWAY) {
                    continue;
                }

                for (var tool: pointType.get(adjPoint).allowedTools) {
                    var nextMinutes = state.minutes + 1 + (tool == state.tool ? 0 : 7);
                    var pair = Pair.of(adjPoint, tool);

                    if (allowedTools.contains(tool)
                        && visitedMinutes.getOrDefault(pair, Integer.MAX_VALUE) > nextMinutes
                    ) {
                        var nextState = new State(
                            tool,
                            adjPoint,
                            nextMinutes
                        );

                        visitedMinutes.put(pair, nextState.minutes);

                        if (nextState.point.equals(target)) {
                            var finalMinutes = nextState.tool == Tool.TORCH ? nextState.minutes : nextState.minutes + 7;

                            if (finalMinutes < minPath) {
                                minPath = finalMinutes;
                            }

                            continue;
                        }

                        queue.add(nextState);
                    }
                }
            }
        }

        return String.valueOf(minPath);
    }

    private long geoIndex(Point p) {
        if (p.x == 0 && p.y == 0) return 0;
        if (p.x == target.x && p.y == target.y) return 0;
        if (p.y == 0) return p.x * 16807L;
        if (p.x == 0) return p.y * 48271L;

        return (long) erosionLevel(new Point(p.x - 1, p.y)) * erosionLevel(new Point(p.x, p.y - 1));
    }

    private int erosionLevel(Point p) {
        return erosionLevels.computeIfAbsent(p, point -> (int) ((geoIndex(point) + depth) % 20183));
    }

    record State(Tool tool, Point point, int minutes) {}

    enum Tool {
        TORCH,
        CLIMBING,
        NEITHER
    }

    enum RegionType {
        ROCKY(0, Tool.CLIMBING, Tool.TORCH),
        WET(1, Tool.CLIMBING, Tool.NEITHER),
        NARROW(2, Tool.TORCH, Tool.NEITHER);

        public final int riskLevel;
        public final Set<Tool> allowedTools;

        RegionType(int riskLevel,  Tool... allowedTools) {
            this.riskLevel = riskLevel;
            this.allowedTools = Set.of(allowedTools);
        }

        public static RegionType ofErosionLevel(long erosionLevel) {
            return switch ((int) (erosionLevel % 3)) {
                case 0 -> ROCKY;
                case 1 -> WET;
                case 2 -> NARROW;
                default -> throw new IllegalStateException("Unexpected value: " + erosionLevel);
            };
        }
    }
}
