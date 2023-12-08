package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.Regex;
import com.vuryss.aoc.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unused")
public class Day6 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "turn on 0,0 through 999,999", "1000000",
            "toggle 0,0 through 999,0", "1000",
            "turn off 499,499 through 500,500", "0"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "turn on 0,0 through 0,0", "1",
            "toggle 0,0 through 999,999", "2000000"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        var grid = new HashMap<Integer, Boolean>();

        for (var line: lines) {
            var coords = StringUtil.ints(line);
            var op = Regex.match("(turn on|turn off|toggle)", line);
            assert op != null;
            Function<Boolean, Boolean> operation = switch (op) {
                case "turn on" -> (b) -> true;
                case "turn off" -> (b) -> false;
                case "toggle" -> (b) -> !b;
                default -> throw new RuntimeException("Unknown operation: " + op);
            };

            for (var x = coords.get(0); x <= coords.get(2); x++) {
                for (var y = coords.get(1); y <= coords.get(3); y++) {
                    int point = x * 1000 + y;
                    grid.put(point, operation.apply(grid.getOrDefault(point, false)));
                }
            }
        }

        return grid.values().stream().filter(Boolean::booleanValue).count() + "";
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.trim().split("\n");
        var grid = new HashMap<Integer, Integer>();

        for (var line: lines) {
            var coords = StringUtil.ints(line);
            var op = Regex.match("(turn on|turn off|toggle)", line);
            assert op != null;
            Function<Integer, Integer> operation = switch (op) {
                case "turn on" -> (i) -> i + 1;
                case "turn off" -> (i) -> Math.max(i - 1, 0);
                case "toggle" -> (i) -> i + 2;
                default -> throw new RuntimeException("Unknown operation: " + op);
            };

            for (var x = coords.get(0); x <= coords.get(2); x++) {
                for (var y = coords.get(1); y <= coords.get(3); y++) {
                    int point = x * 1000 + y;
                    grid.put(point, operation.apply(grid.getOrDefault(point, 0)));
                }
            }
        }

        return grid.values().stream().mapToInt(Integer::intValue).sum() + "";
    }
}
