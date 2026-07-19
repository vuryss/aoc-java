package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class Day24 implements SolutionInterface {
    private static final int TOP_EDGE_MASK = 1 + (1 << 1) + (1 << 2) + (1 << 3) + (1 << 4);
    private static final int LEFT_EDGE_MASK = 1 + (1 << 5) + (1 << 10) + (1 << 15) + (1 << 20);
    private static final int BOTTOM_EDGE_MASK = (1 << 20) + (1 << 21) + (1 << 22) + (1 << 23) + (1 << 24);
    private static final int RIGHT_EDGE_MASK = (1 << 4) + (1 << 9) + (1 << 14) + (1 << 19) + (1 << 24);

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            ....#
            #..#.
            #..##
            ..#..
            #....
            """,
            "2129920"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            ....#
            #..#.
            #..##
            ..#..
            #....
            """,
            "99"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var layout = parseGridToBits(input);
        var seen = new HashSet<Integer>();

        seen.add(layout);

        while (true) {
            var nextLayout = layout;

            for (var i = 0; i < 25; i++) {
                int left   = i % 5 == 0       ? 0 : bug(layout, i - 1);
                int right  = (i + 1) % 5 == 0 ? 0 : bug(layout, i + 1);
                int top    = i < 5            ? 0 : bug(layout, i - 5);
                int bottom = i > 19           ? 0 : bug(layout, i + 5);
                var count = left + right + top + bottom;
                var hasBug = (layout & (1 << i)) > 0;

                if (hasBug && count != 1) nextLayout = nextLayout & ~(1 << i);
                else if (!hasBug && count > 0 && count < 3) nextLayout = nextLayout | (1 << i);
            }

            if (!seen.add(nextLayout)) return nextLayout + "";
            layout = nextLayout;
        }
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var state = new HashMap<Integer, Integer>();

        state.put(0, parseGridToBits(input));

        for (int m = 0, totalMinutes = isTest ? 10 : 200; m < totalMinutes; m++) {
            var newState = new HashMap<>(state);
            var minDepth = 0;
            var maxDepth = 0;

            for (var es: state.entrySet()) {
                var depth = es.getKey();
                var layout = es.getValue();

                minDepth = Math.min(minDepth, depth);
                maxDepth = Math.max(maxDepth, depth);
                newState.put(depth, calculate(depth, state, layout));
            }

            var previousDepth = calculate(minDepth - 1, state, 0);
            var nextDepth = calculate(maxDepth + 1, state, 0);

            if (previousDepth > 0) newState.put(minDepth - 1, previousDepth);
            if (nextDepth > 0) newState.put(maxDepth + 1, nextDepth);

            state = newState;
        }

        return state.values().stream().mapToInt(Integer::bitCount).sum() + "";
    }

    private int parseGridToBits(String input) {
        var grid = StringUtil.toCharGrid(input.trim());
        var layout = 0;

        for (var y = 0; y < grid.length; y++)
            for (var x = 0; x < grid[y].length; x++)
                if (grid[y][x] == '#') layout |= 1 << (y * 5 + x);

        return layout;
    }

    private int calculate(int depth, Map<Integer, Integer> state, int layout) {
        int left, right, top, bottom, nextLayout = layout;
        int previousDepth = state.getOrDefault(depth - 1, 0);
        int nextDepth = state.getOrDefault(depth + 1, 0);

        for (var i = 0; i < 25; i++) {
            if (i == 12) continue; // Skip middle as it's recursive deeper level

            if (i == 13) left = bugsOnEdge(nextDepth, RIGHT_EDGE_MASK);
            else left = i % 5 == 0 ? bug(previousDepth, 11) : bug(layout, i - 1);

            if (i == 11) right = bugsOnEdge(nextDepth, LEFT_EDGE_MASK);
            else right = (i + 1) % 5 == 0 ? bug(previousDepth, 13) : bug(layout, i + 1);

            if (i == 17) top = bugsOnEdge(nextDepth, BOTTOM_EDGE_MASK);
            else top = i < 5 ? bug(previousDepth, 7) : bug(layout, i - 5);

            if (i == 7) bottom = bugsOnEdge(nextDepth, TOP_EDGE_MASK);
            else bottom = i > 19 ? bug(previousDepth, 17) : bug(layout, i + 5);

            var count = left + right + top + bottom;
            var hasBug = (layout & (1 << i)) > 0;

            if (hasBug && count != 1) nextLayout = nextLayout & ~(1 << i);
            else if (!hasBug && count > 0 && count < 3) nextLayout = nextLayout | (1 << i);
        }

        return nextLayout;
    }

    private int bug(int layout, int position) {
        return (layout & (1 << position)) > 0 ? 1 : 0;
    }

    private int bugsOnEdge(int layout, int mask) {
        return Integer.bitCount(layout & mask);
    }
}