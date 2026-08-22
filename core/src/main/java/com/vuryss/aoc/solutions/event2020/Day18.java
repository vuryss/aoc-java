package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.util.*;
import java.util.function.ToLongFunction;

@SuppressWarnings("unused")
public class Day18 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "1 + 2 * 3 + 4 * 5 + 6", "71",
            "1 + (2 * 3) + (4 * (5 + 6))", "51",
            "2 * 3 + (4 * 5)", "26",
            "5 + (8 * 3 + 9 + 3 * 4 * 3)", "437",
            "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))", "12240",
            "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", "13632"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "1 + 2 * 3 + 4 * 5 + 6", "231",
            "1 + (2 * 3) + (4 * (5 + 6))", "51",
            "2 * 3 + (4 * 5)", "46",
            "5 + (8 * 3 + 9 + 3 * 4 * 3)", "1445",
            "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))", "669060",
            "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", "23340"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return input.trim().lines()
            .mapToLong(line -> solve(tokenize(line), new Meta(), this::noPrecedence))
            .sum() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return input.trim().lines()
            .mapToLong(line -> solve(tokenize(line), new Meta(), this::additionPrecedence))
            .sum() + "";
    }

    private long solve(Token[] equation, Meta meta, ToLongFunction<Deque<Token>> strategy) {
        var tokens = new ArrayDeque<Token>();

        for (int i = meta.position; i < equation.length; i++) {
            switch (equation[i]) {
                case NumberToken nt -> tokens.addLast(nt);
                case AdditionToken add -> tokens.addLast(add);
                case MultiplicationToken multi -> tokens.addLast(multi);
                case OpenParenthesisToken op -> {
                    meta.position = i + 1;
                    tokens.addLast(new NumberToken(solve(equation, meta, strategy)));
                    i = meta.position;
                }
                case CloseParenthesisToken cp -> {
                    meta.position = i;
                    return strategy.applyAsLong(tokens);
                }
            }
        }

        return strategy.applyAsLong(tokens);
    }

    private long noPrecedence(Deque<Token> tokens) {
        long result = ((NumberToken) tokens.removeFirst()).value();

        while (!tokens.isEmpty()) {
            var operation = tokens.removeFirst();
            var value = ((NumberToken) tokens.removeFirst()).value();

            if (operation instanceof AdditionToken) result += value;
            else result *= value;
        }

        return result;
    }

    private long additionPrecedence(Deque<Token> tokens) {
        long result = 1;
        long addition = ((NumberToken) tokens.removeFirst()).value();

        while (!tokens.isEmpty()) {
            var operation = tokens.removeFirst();
            var value = ((NumberToken) tokens.removeFirst()).value();

            if (operation instanceof AdditionToken) addition += value;
            else {
                result *= addition;
                addition = value;
            }
        }

        return result * addition;
    }

    private Token[] tokenize(String equation) {
        var tokens = new ArrayList<Token>();
        var chars = equation.toCharArray();

        for (var i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                var sb = new StringBuilder();
                while (i < chars.length && Character.isDigit(chars[i])) sb.append(chars[i++]);
                i--;
                tokens.add(new NumberToken(Long.parseLong(sb.toString())));
            } else if (chars[i] == '(') {
                tokens.add(new OpenParenthesisToken());
            } else if (chars[i] == ')') {
                tokens.add(new CloseParenthesisToken());
            } else if (chars[i] == '+') {
                tokens.add(new AdditionToken());
            } else if (chars[i] == '*') {
                tokens.add(new MultiplicationToken());
            }
        }

        return tokens.toArray(new Token[0]);
    }

    private static class Meta { public int position = 0; }
    private sealed interface Token {}
    private record NumberToken(long value) implements Token {}
    private static final class AdditionToken implements Token {}
    private static final class MultiplicationToken implements Token {}
    private static final class OpenParenthesisToken implements Token {}
    private static final class CloseParenthesisToken implements Token {}
}
