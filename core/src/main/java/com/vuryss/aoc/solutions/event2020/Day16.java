package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;
import org.apache.commons.lang3.Range;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day16 implements SolutionInterface {
    private static final Pattern pattern = Pattern.compile("(.+):\\s(\\d+)-(\\d+)\\sor\\s(\\d+)-(\\d+)");

    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var parts = input.trim().split("\n\n");
        var rules = parseRules(parts[0]);
        var allRanges = rules.values().stream().flatMap(Collection::stream).toList();

        return parts[2].lines()
            .skip(1)
            .flatMap(line -> Arrays.stream(line.split(",")).map(Integer::valueOf))
            .filter(value -> allRanges.stream().noneMatch(r -> r.contains(value)))
            .mapToInt(Integer::intValue).sum() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var parts = input.trim().split("\n\n");
        var rules = parseRules(parts[0]);
        var allRanges = rules.values().stream().flatMap(Collection::stream).toList();
        var tickets = parts[2].lines()
            .skip(1)
            .map(line -> Arrays.stream(line.split(",")).map(Integer::parseInt).toList())
            .filter(
                nums -> nums.stream().allMatch(n -> allRanges.stream().anyMatch(r -> r.contains(n)))
            )
            .toList();
        var fields = tickets.getFirst().size();
        var foundPosition = new String[fields];
        var myNumbers = StringUtil.ints(parts[1]);
        long product = 1;

        while (!rules.isEmpty()) {
            for (var i = 0; i < fields; i++) {
                if (foundPosition[i] != null) continue;
                final var pos = i;
                var numbers = tickets.stream().map(list -> list.get(pos)).toList();

                var matchingRules = rules.entrySet().stream().filter(
                    e -> numbers.stream().allMatch(
                        n -> e.getValue().stream().anyMatch(range -> range.contains(n))
                    )
                ).toList();

                if (matchingRules.size() == 1) {
                    var rule = matchingRules.getFirst().getKey();
                    foundPosition[i] = rule;
                    rules.remove(rule);
                }
            }
        }

        for (var i = 0; i < foundPosition.length; i++)
            if (foundPosition[i].startsWith("departure")) product *= myNumbers.get(i);

        return product + "";
    }

    private Map<String, List<Range<Integer>>> parseRules(String input) {
        var rules = new HashMap<String, List<Range<Integer>>>();

        input.lines().forEach(line -> {
            var m = pattern.matcher(line);
            if (!m.find()) throw new RuntimeException("Unable to parse line: " + line);
            var r1 = Range.of(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
            var r2 = Range.of(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)));
            rules.put(m.group(1), List.of(r1, r2));
        });

        return rules;
    }
}