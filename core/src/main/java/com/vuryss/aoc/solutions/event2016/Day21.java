package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unused")
public class Day21 implements SolutionInterface {
    private static final Map<Integer, Integer> MOD_8_REVERSE = Map.of(
        0, 7, // 7 + 7 + 2 = 16 % 8 = 0
        1, 0, // 0 + 0 + 1 = 1  % 8 = 1
        2, 4, // 4 + 4 + 2 = 10 % 8 = 2
        3, 1, // 1 + 1 + 1 = 3  % 8 = 3
        4, 5, // 5 + 5 + 2 = 12 % 8 = 4
        5, 2, // 2 + 2 + 1 = 5  % 8 = 5
        6, 6, // 6 + 6 + 2 = 14 % 8 = 6
        7, 3  // 3 + 3 + 1 = 7  % 8 = 7
    );

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            swap position 4 with position 0
            swap letter d with letter b
            reverse positions 0 through 4
            rotate left 1 step
            move position 1 to position 4
            move position 3 to position 0
            rotate based on position of letter b
            rotate based on position of letter d
            """,
            "decab"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var password = isTest ? "abcde" : "abcdefgh";

        for (var operation : parseOperations(input)) {
            password = operation.apply(password);
        }

        return password;
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var operations = parseReversedOperations(input);
        var password = "fbgdceah";

        for (int i = operations.size() - 1; i >= 0; i--) {
            password = operations.get(i).apply(password);
        }

        return password;
    }

    private List<Function<String, String>> parseOperations(String input) {
        return input.trim().lines()
            .map(line -> {
                String[] parts = line.split(" ");
                String opType = parts[0] + "_" + parts[1];

                return (Function<String, String>) str -> switch (opType) {
                    case "swap_position" -> Operation.swapPosition(str, Integer.parseInt(parts[2]), Integer.parseInt(parts[5]));
                    case "swap_letter" -> Operation.swapLetter(str, parts[2].charAt(0), parts[5].charAt(0));
                    case "rotate_left" -> Operation.rotateLeft(str, Integer.parseInt(parts[2]));
                    case "rotate_right" -> Operation.rotateRight(str, Integer.parseInt(parts[2]));
                    case "rotate_based" -> Operation.rotateBased(str, parts[6].charAt(0));
                    case "reverse_positions" -> Operation.reverse(str, Integer.parseInt(parts[2]), Integer.parseInt(parts[4]));
                    case "move_position" -> Operation.move(str, Integer.parseInt(parts[2]), Integer.parseInt(parts[5]));
                    default -> throw new IllegalArgumentException("Unknown operation: " + opType);
                };
            })
            .toList();
    }

    private List<Function<String, String>> parseReversedOperations(String input) {
        return input.trim().lines()
            .map(line -> {
                String[] parts = line.split(" ");
                String opType = parts[0] + "_" + parts[1];

                return (Function<String, String>) str -> switch (opType) {
                    case "swap_position" -> Operation.swapPosition(str, Integer.parseInt(parts[5]), Integer.parseInt(parts[2]));
                    case "swap_letter" -> Operation.swapLetter(str, parts[5].charAt(0), parts[2].charAt(0));
                    case "rotate_left" -> Operation.rotateRight(str, Integer.parseInt(parts[2]));
                    case "rotate_right" -> Operation.rotateLeft(str, Integer.parseInt(parts[2]));
                    case "rotate_based" -> Operation.rotateBasedReversed(str, parts[6].charAt(0));//
                    case "reverse_positions" -> Operation.reverse(str, Integer.parseInt(parts[2]), Integer.parseInt(parts[4]));
                    case "move_position" -> Operation.move(str, Integer.parseInt(parts[5]), Integer.parseInt(parts[2]));
                    default -> throw new IllegalArgumentException("Unknown operation: " + opType);
                };
            })
            .toList();
    }

    private static class Operation {
        public static String swapPosition(String input, int x, int y) {
            var chars = input.toCharArray();
            var temp = chars[x];
            chars[x] = chars[y];
            chars[y] = temp;
            return new String(chars);
        }

        public static String swapLetter(String input, char x, char y) {
            return input.replace(x, '_').replace(y, x).replace('_', y);
        }

        public static String rotateLeft(String input, int steps) {
            steps %= input.length();
            return input.substring(steps) + input.substring(0, steps);
        }

        public static String rotateRight(String input, int steps) {
            steps %= input.length();
            return input.substring(input.length() - steps) + input.substring(0, input.length() - steps);
        }

        public static String rotateBased(String input, char x) {
            var index = input.indexOf(x);
            return rotateRight(input, index + 1 + (index >= 4 ? 1 : 0));
        }

        public static String rotateBasedReversed(String input, char x) {
            var index = input.indexOf(x);
            var diff = MOD_8_REVERSE.get(index) - index;
            return diff < 0 ? rotateLeft(input, -diff) : rotateRight(input, diff);
        }

        public static String reverse(String input, int x, int y) {
            return input.substring(0, x) + new StringBuilder(input.substring(x, y + 1)).reverse() + input.substring(y + 1);
        }

        public static String move(String input, int x, int y) {
            var chars = new StringBuilder(input);
            var removed = chars.charAt(x);
            chars.deleteCharAt(x);
            chars.insert(y, removed);
            return chars.toString();
        }
    }
}
