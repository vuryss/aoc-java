package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Direction;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.StringUtil;
import com.vuryss.aoc.util.Util;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class Day17 implements SolutionInterface {
    private final static String gridAsString = """
        #########
        #S| | | #
        #-#-#-#-#
        # | | | #
        #-#-#-#-#
        # | | | #
        #-#-#-#-#
        # | | | \s
        ####### V
        """;
    private final static Direction[] directions = new Direction[] { Direction.U, Direction.D, Direction.L, Direction.R };

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "ihgpwlah", "DDRRRD",
            "kglvqrro", "DDUDRLRRUDRD",
            "ulqzkmiv", "DRURDRUDDLLDLUURRDULRLDUUDDDRR"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "ihgpwlah", "370",
            "kglvqrro", "492",
            "ulqzkmiv", "830"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var grid = Util.inputToGrid(gridAsString);
        var passcode = input.trim();
        var startPosition = new Point(1, 1);
        var targetPosition = new Point(7, 7);
        var queue = new LinkedList<State>() {{ add(new State(startPosition, "")); }};

        while (!queue.isEmpty()) {
            var state = queue.poll();

            if (state.position.equals(targetPosition)) {
                return state.path;
            }

            var hash = StringUtil.md5(passcode + state.path);

            IntStream.range(0, 4)
                .filter(i -> hash.charAt(i) >= 'b' && hash.charAt(i) <= 'f')
                .mapToObj(i -> directions[i])
                .filter(d -> grid.get(state.position.goInDirection(d)) != '#')
                .forEach(d -> queue.add(new State(state.position.goInDirection(d, 2), state.path + d.getChar())));
        }

        return "-not found-";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var grid = Util.inputToGrid(gridAsString);
        var passcode = input.trim();
        var startPosition = new Point(1, 1);
        var targetPosition = new Point(7, 7);
        var queue = new LinkedList<State>() {{ add(new State(startPosition, "")); }};
        var maxPathLength = 0;

        while (!queue.isEmpty()) {
            var state = queue.poll();

            if (state.position.equals(targetPosition)) {
                maxPathLength = state.path.length();
                continue;
            }

            var hash = StringUtil.md5(passcode + state.path);

            IntStream.range(0, 4)
                .filter(i -> hash.charAt(i) >= 'b' && hash.charAt(i) <= 'f')
                .mapToObj(i -> directions[i])
                .filter(d -> grid.get(state.position.goInDirection(d)) != '#')
                .forEach(d -> queue.add(new State(state.position.goInDirection(d, 2), state.path + d.getChar())));
        }

        return String.valueOf(maxPathLength);
    }

    private record State(Point position, String path) {}
}
