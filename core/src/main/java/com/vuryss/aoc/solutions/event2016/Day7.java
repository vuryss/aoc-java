package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;

import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day7 implements SolutionInterface {
    private static final Pattern SUPERNET_PATTERN = Pattern.compile("(^|])([^\\[]+)(\\[|$)");
    private static final Pattern HYPERNET_PATTERN = Pattern.compile("\\[([^]]+)]");

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            abba[mnop]qrst
            abcd[bddb]xyyx
            aaaa[qwer]tyui
            ioxxoj[asdfgh]zxcvbn
            """,
            "2"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            aba[bab]xyz
            xyx[xyx]xyx
            aaa[kek]eke
            zazbz[bzb]cdb
            """,
            "3"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return String.valueOf(
            Arrays.stream(input.trim().split("\n"))
                .filter(this::supportsTLS)
                .count()
        );
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return String.valueOf(
            Arrays.stream(input.trim().split("\n"))
                .filter(this::supportsSSL)
                .count()
        );
    }

    private List<String> getSupernetSequences(String line) {
        return SUPERNET_PATTERN.matcher(line).results().map(m -> m.group(2)).toList();
    }

    private List<String> getHypernetSequences(String line) {
        return HYPERNET_PATTERN.matcher(line).results().map(m -> m.group(1)).toList();
    }

    private boolean supportsSSL(String line) {
        var supernetSequences = getSupernetSequences(line);
        var hypernetSequences = getHypernetSequences(line);
        var babs = supernetSequences.stream()
            .flatMap(seq -> Regex.matchAllOverlapping("(\\w)(?!\\1)(\\w)\\1", seq).stream())
            .map(aba -> aba.substring(1) + aba.charAt(1))
            .toList();

        return hypernetSequences.stream().anyMatch(seq -> babs.stream().anyMatch(seq::contains));
    }

    private boolean supportsTLS(String line) {
        var supernetSequences = getSupernetSequences(line);
        var hypernetSequences = getHypernetSequences(line);

        return supernetSequences.stream().anyMatch(this::hasABBA)
            && hypernetSequences.stream().noneMatch(this::hasABBA);
    }

    private boolean hasABBA(String line) {
        return Regex.matches("(\\w)(?!\\1)(\\w)\\2\\1", line);
    }
}
