package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day19 implements SolutionInterface {
    private Map<Integer, Node> rules;

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            0: 4 1 5
            1: 2 3 | 3 2
            2: 4 4 | 5 5
            3: 4 5 | 5 4
            4: "a"
            5: "b"
            
            ababbb
            bababa
            abbbab
            aaabbb
            aaaabbb
            """,
            "2"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            42: 9 14 | 10 1
            9: 14 27 | 1 26
            10: 23 14 | 28 1
            1: "a"
            11: 42 31
            5: 1 14 | 15 1
            19: 14 1 | 14 14
            12: 24 14 | 19 1
            16: 15 1 | 14 14
            31: 14 17 | 1 13
            6: 14 14 | 1 14
            2: 1 24 | 14 4
            0: 8 11
            13: 14 3 | 1 12
            15: 1 | 14
            17: 14 2 | 1 7
            23: 25 1 | 22 14
            28: 16 1
            4: 1 1
            20: 14 14 | 1 15
            3: 5 14 | 16 1
            27: 1 6 | 14 18
            14: "b"
            21: 14 1 | 1 14
            25: 1 1 | 1 14
            22: 14 14
            8: 42
            26: 14 22 | 1 20
            18: 15 15
            7: 14 5 | 1 21
            24: 14 1
            
            abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa
            bbabbbbaabaabba
            babbbbaabbbbbabbbbbbaabaaabaaa
            aaabbbbbbaaaabaababaabababbabaaabbababababaaa
            bbbbbbbaaaabbbbaaabbabaaa
            bbbababbbbaaaaaaaabbababaaababaabab
            ababaaaaaabaaab
            ababaaaaabbbaba
            baabbaaaabbaaaababbaababb
            abbbbabbbbaaaababbbbbbaaaababb
            aaaaabbaabaaaaababaa
            aaaabbaaaabbaaa
            aaaabbaabbaaaaaaabbbabbbaaabbaabaaa
            babaaabbbaaabaababbaabababaaab
            aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba
            """,
            "12"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var parts = input.trim().split("\n\n");
        rules = parseRules(parts[0]);

        return Arrays.stream(parts[1].split("\n"))
            .map(String::toCharArray)
            .filter(m -> matches(m, rules.get(0), 0).contains(m.length))
            .count() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var parts = input.trim().split("\n\n");
        rules = parseRules(parts[0]);
        rules.put(
            8,
            new OrNode(8, new Node[] {new RefNode(42)}, new Node[] {new RefNode(42), new RefNode(8)})
        );
        rules.put(
            11,
            new OrNode(11, new Node[] {new RefNode(42), new RefNode(31)}, new Node[] {new RefNode(42), new RefNode(11), new RefNode(31)})
        );

        return Arrays.stream(parts[1].split("\n"))
            .map(String::toCharArray)
            .filter(m -> matches(m, rules.get(0), 0).contains(m.length))
            .count() + "";
    }

    private Set<Integer> matches(char[] message, Node rule, int index) {
        return switch (rule) {
            case OrNode or -> {
                var leftPossible = matchSequence(message, or.left, index);
                var rightPossible = matchSequence(message, or.right, index);
                var joined = new HashSet<>(leftPossible);
                joined.addAll(rightPossible);
                yield joined;
            }
            case SequenceNode sn -> matchSequence(message, sn.sequence, index);
            case LiteralNode ln -> {
                if (index < message.length && message[index] == ln.value) yield Set.of(index + 1);
                else yield Set.of();
            }
            case RefNode rn -> matches(message, rules.get(rn.id()), index);
        };
    }

    private Set<Integer> matchSequence(char[] message, Node[] sequence, int index) {
        var possible = Set.of(index);

        for (final Node currentRule : sequence) {
            possible = possible.stream()
                .flatMap(idx -> matches(message, currentRule, idx).stream())
                .collect(Collectors.toSet());
        }

        return possible;
    }

    private Map<Integer, Node> parseRules(String rules) {
        var map = new HashMap<Integer, Node>();
        var lines = rules.trim().split("\n");

        for (var line : lines) {
            var split1 = line.split(": ");
            int id = Integer.parseInt(split1[0]);
            var rule = split1[1];

            if (rule.charAt(0) == '"') {
                map.put(id, new LiteralNode(id, rule.charAt(1)));
                continue;
            }

            if (rule.contains(" | ")) {
                var split2 = rule.split("\\s\\|\\s");
                var left = StringUtil.ints(split2[0]);
                var right = StringUtil.ints(split2[1]);
                map.put(
                    id,
                    new OrNode(
                        id,
                        left.stream().map(RefNode::new).toArray(Node[]::new),
                        right.stream().map(RefNode::new).toArray(Node[]::new)
                    )
                );
                continue;
            }

            var ids = StringUtil.ints(rule);
            map.put(
                id,
                new SequenceNode(id, ids.stream().map(RefNode::new).toArray(Node[]::new))
            );
        }

        return map;
    }

    private sealed interface Node { int id(); }
    private record OrNode(int id, Node[] left, Node[] right) implements Node {}
    private record SequenceNode(int id, Node[] sequence) implements Node {}
    private record LiteralNode(int id, char value) implements Node {}
    private record RefNode(int id) implements Node {}
}
