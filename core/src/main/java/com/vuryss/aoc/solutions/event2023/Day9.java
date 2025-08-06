package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.util.*;

@SuppressWarnings("unused")
public class Day9 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """,
            "114"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """,
            "2"
        );
    }

    /**
     * This is a linear solution using pascal's triangle. O(n) per line
     */
    @Override
    public String part1Solution(String input) {
        var lines = input.trim().split("\n");
        var sum = 0L;

        for (var line: lines) {
            var numbers = StringUtil.slongs(line);
            var size = numbers.size();
            var coeffTrim = size % 2 == 0 ? 1 : 0;
            var coeff = 1L;
            List<Long> coeffs = new ArrayList<>(){{ add(1L); }};
            var num = 0L;
            var n = -1;

            for (var i = 1; i <= size / 2; i++) {
                coeff = coeff * (size - i + 1) / i;
                coeffs.add(coeff);
            }

            coeffs.addAll(coeffs.reversed().subList(coeffTrim, coeffs.size()));

            for (var i = 0; i < size; i++) {
                num += coeffs.get(i + 1) * numbers.removeLast() * n;
                n *= -1;
            }

            sum -= num;
        }

        return String.valueOf(sum);
    }

    /**
     * This is a O(n^2) per line solution following the instructions from the puzzle.
     */
    @Override
    public String part2Solution(String input) {
        return String.valueOf(
            Arrays.stream(input.trim().split("\n"))
                .map(StringUtil::slongs)
                .map(this::expandedLists)
                .map(List::reversed)
                .map(l -> l.stream().reduce(0L, (a, b) -> b.getFirst() - a, Long::sum))
                .mapToLong(Long::longValue)
                .sum()
        );
    }

    private List<List<Long>> expandedLists(List<Long> numbers) {
        var lists = new ArrayList<List<Long>>();
        lists.add(numbers);

        var diffList = numbers;

        do {
            var nextDiffList = new LinkedList<Long>();

            for (var i = 0; i < diffList.size() - 1; i++) {
                nextDiffList.add(diffList.get(i + 1) - diffList.get(i));
            }

            lists.add(nextDiffList);
            diffList = nextDiffList;
        } while (diffList.stream().anyMatch(e -> e != 0));

        lists.removeLast();

        return lists;
    }
}
