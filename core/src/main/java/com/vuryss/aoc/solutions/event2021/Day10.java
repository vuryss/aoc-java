package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.util.*;

@SuppressWarnings("unused")
public class Day10 implements SolutionInterface {
    private final Set<Character> OPENING = Set.of('(', '[', '{', '<');
    private final Map<Character, Character> CLOSING = Map.of('(', ')', '[', ']', '{', '}', '<', '>');

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            [({(<(())[]>[[{[]{<()<>>
            [(()[<>])]({[<{<<[]>>(
            {([(<{}[<>[]}>{[]{[(<()>
            (((({<>}<{<{<>}{[]{[]{}
            [[<[([]))<([[{}[[()]]]
            [{[{({}]{}}([{[{{{}}([]
            {<[[]]>}<{[{[{[]{()[[[]
            [<(<(<(<{}))><([]([]()
            <{([([[(<>()){}]>(<<{{
            <{([{{}}[<[[[<>{}]]]>[]]
            """,
            "26397"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            [({(<(())[]>[[{[]{<()<>>
            [(()[<>])]({[<{<<[]>>(
            {([(<{}[<>[]}>{[]{[(<()>
            (((({<>}<{<{<>}{[]{[]{}
            [[<[([]))<([[{}[[()]]]
            [{[{({}]{}}([{[{{{}}([]
            {<[[]]>}<{[{[{[]{()[[[]
            [<(<(<(<{}))><([]([]()
            <{([([[(<>()){}]>(<<{{
            <{([{{}}[<[[[<>{}]]]>[]]
            """,
            "288957"
        );
    }

    @Override
    public String part1Solution(String input) {
        var lines = input.split("\n");
        var illegalCharsCount = new HashMap<Character, Integer>();

        outer:
        for (var line: lines) {
            var stack = new Stack<Character>();

            for (var c: line.toCharArray()) {
                if (OPENING.contains(c)) {
                    stack.push(CLOSING.get(c));
                    continue;
                }

                var expected = stack.pop();

                if (expected != c) {
                    illegalCharsCount.put(c, illegalCharsCount.getOrDefault(c, 0) + 1);
                    continue outer;
                }
            }
        }

        var totalScore = 0;

        for (var entry: illegalCharsCount.entrySet()) {
            totalScore += switch (entry.getKey()) {
                case ')' -> 3;
                case ']' -> 57;
                case '}' -> 1197;
                case '>' -> 25137;
                default -> 0;
            } * entry.getValue();
        }

        return String.valueOf(totalScore);
    }

    @Override
    public String part2Solution(String input) {
        var lines = input.split("\n");
        var scores = new ArrayList<Long>();

        outer:
        for (var line: lines) {
            var stack = new Stack<Character>();
            var score = 0L;

            for (var c: line.toCharArray()) {
                if (OPENING.contains(c)) {
                    stack.push(CLOSING.get(c));
                    continue;
                }

                if (c != stack.pop()) {
                    continue outer;
                }
            }

            while (!stack.isEmpty()) {
                score = score * 5 + switch (stack.pop()) {
                    case ')' -> 1;
                    case ']' -> 2;
                    case '}' -> 3;
                    case '>' -> 4;
                    default -> 0;
                };
            }

            scores.add(score);
        }

        Collections.sort(scores);

        return String.valueOf(scores.get(scores.size() / 2));
    }
}
