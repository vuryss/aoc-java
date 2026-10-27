package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day18 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
            [[[5,[2,8]],4],[5,[[9,9],0]]]
            [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
            [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
            [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
            [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
            [[[[5,4],[7,7]],8],[[8,3],8]]
            [[9,3],[[9,9],[6,[4,9]]]]
            [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
            [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
            """,
            "4140"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
            [[[5,[2,8]],4],[5,[[9,9],0]]]
            [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
            [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
            [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
            [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
            [[[[5,4],[7,7]],8],[[8,3],8]]
            [[9,3],[[9,9],[6,[4,9]]]]
            [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
            [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
            """,
            "3993"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var lines = input.lines().collect(Collectors.toList());
        var number = new SnailfishNumber(lines.removeFirst());

        for (var line : lines) {
            number = number.add(new SnailfishNumber(line));
        }

        return String.valueOf(number.magnitude());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var numbers = input.lines().map(SnailfishNumber::new).toList();
        var maxMagnitude = 0;

        for (var i = 0; i < numbers.size(); i++) {
            var number1 = numbers.get(i);

            for (var j = 0; j < numbers.size(); j++) {
                if (i == j) {
                    continue;
                }

                maxMagnitude = Math.max(maxMagnitude, number1.add(numbers.get(j)).magnitude());
            }
        }

        return String.valueOf(maxMagnitude);
    }
}

class SnailfishNumber {
    public String value;

    public SnailfishNumber(String value) {
        this.value = value;
    }

    public SnailfishNumber add(SnailfishNumber other) {
        var newNumber = new SnailfishNumber("[" + this.value + "," + other.value + "]");
        newNumber.reduce();

        return newNumber;
    }

    public void reduce() {
        if (explode()) {
            reduce();
            return;
        }

        if (split()) {
            reduce();
        }
    }

    public int magnitude() {
        var tokens = getValueTokens();

        int result = 0;
        var multiplierStack = new ArrayList<Integer>();

        for (var token : tokens) {
            if (token.type == TokenType.OPEN_BRACKET) {
                multiplierStack.add(3);
                continue;
            }

            if (token.type == TokenType.CLOSE_BRACKET) {
                multiplierStack.removeLast();
                continue;
            }

            if (token.type == TokenType.NUMBER) {
                var currentMultiplier = 1;

                for (var multiplier : multiplierStack) {
                    currentMultiplier *= multiplier;
                }

                result += Integer.parseInt(token.value) * currentMultiplier;
                continue;
            }

            if (token.type == TokenType.COMMA) {
                multiplierStack.removeLast();
                multiplierStack.add(2);
            }
        }

        return result;
    }

    private boolean explode() {
        var tokens = getValueTokens();
        var level = 0;
        Token previousNumber = null;
        Token leftNumber = null;
        Token rightNumber = null;
        Token nextNumber = null;

        for (var i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i);

            if (token.type == TokenType.OPEN_BRACKET) {
                level++;
                continue;
            }

            if (token.type == TokenType.CLOSE_BRACKET) {
                level--;
                continue;
            }

            if (token.type != TokenType.NUMBER) {
                continue;
            }

            if (level != 5) {
                previousNumber = token;
                continue;
            }

            if (null == leftNumber) {
                leftNumber = token;
                continue;
            }

            rightNumber = token;

            for (var j = i + 1; j < tokens.size(); j++) {
                if (tokens.get(j).type == TokenType.NUMBER) {
                    nextNumber = tokens.get(j);
                    break;
                }
            }

            // From right to left, so indexes can be kept, we replace the next number to the right, if exists
            if (null != nextNumber) {
                var newNextNumber = Integer.parseInt(nextNumber.value) + Integer.parseInt(rightNumber.value);
                value = value.substring(0, nextNumber.startIndex) + newNextNumber + value.substring(nextNumber.endIndex);
            }

            // Then we remove the current pair
            value = value.substring(0, leftNumber.startIndex - 1) + "0" + value.substring(rightNumber.endIndex + 1);

            // And replace the left number to the left, if exists
            if (null != previousNumber) {
                var newPreviousNumber = Integer.parseInt(previousNumber.value) + Integer.parseInt(leftNumber.value);
                value = value.substring(0, previousNumber.startIndex) + newPreviousNumber + value.substring(previousNumber.endIndex);
            }

            return true;
        }

        return false;
    }

    private boolean split() {
        if (!value.matches(".*\\d{2,}.*")) {
            return false;
        }

        var tokens = getValueTokens();

        for (Token token : tokens) {
            if (token.type != TokenType.NUMBER || Integer.parseInt(token.value) < 10) {
                continue;
            }

            value = value.substring(0, token.startIndex)
                + "[" + (int) Math.floor(Integer.parseInt(token.value) / 2.0) + "," + (int) Math.ceil(Integer.parseInt(token.value) / 2.0) + "]"
                + value.substring(token.endIndex);

            return true;
        }

        return false;
    }

    private ArrayList<Token> getValueTokens()
    {
        var tokens = new ArrayList<Token>();

        for (var i = 0; i < value.length(); i++) {
            var c = value.charAt(i);

            if (c == '[') {
                tokens.add(new Token("[", i, TokenType.OPEN_BRACKET));
            } else if (c == ']') {
                tokens.add(new Token("]", i, TokenType.CLOSE_BRACKET));
            } else if (c >= '0' && c <= '9') {
                var start = i;
                var end = i;

                while (end < value.length() && value.charAt(end) >= '0' && value.charAt(end) <= '9') {
                    end++;
                }

                tokens.add(new Token(value.substring(start, end), start, TokenType.NUMBER));
                i = end - 1;
            } else if (c == ',') {
                tokens.add(new Token(",", i, TokenType.COMMA));
            }
        }

        return tokens;
    }
}

enum TokenType {
    NUMBER,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    COMMA
}

class Token {
    public String value;
    public int startIndex;
    public int endIndex;
    public TokenType type;

    public Token(String value, int startIndex, TokenType type) {
        this.value = value;
        this.startIndex = startIndex;
        this.endIndex = startIndex + value.length();
        this.type = type;
    }
}