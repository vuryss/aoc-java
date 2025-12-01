package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("unused")
public class Day8 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2", "138");
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2", "66");
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var numbers = new ArrayDeque<>(StringUtil.ints(input));
        var rootNode = parseNodes(numbers);

        return rootNode.getAllMetadataSum() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var numbers = new ArrayDeque<>(StringUtil.ints(input));
        var rootNode = parseNodes(numbers);

        return rootNode.getValue() + "";
    }

    private Node parseNodes(ArrayDeque<Integer> numbers) {
        var node = new Node();
        node.childCount = numbers.removeFirst();
        node.metadataCount = numbers.removeFirst();

        for (var i = 0; i < node.childCount; i++) {
            node.children.add(parseNodes(numbers));
        }

        for (var i = 0; i < node.metadataCount; i++) {
            node.metadata.add(numbers.removeFirst());
        }

        return node;
    }

    private static class Node {
        public int childCount;
        public int metadataCount;
        public ArrayList<Node> children = new ArrayList<>();
        public ArrayList<Integer> metadata = new ArrayList<>();

        public int getAllMetadataSum() {
            return metadata.stream().mapToInt(Integer::intValue).sum() + children.stream().mapToInt(Node::getAllMetadataSum).sum();
        }

        public int getValue() {
            if (children.isEmpty()) {
                return metadata.stream().mapToInt(Integer::intValue).sum();
            }

            return metadata.stream()
                .filter(index -> index > 0 && index <= children.size())
                .mapToInt(index -> children.get(index - 1).getValue())
                .sum();
        }
    }
}
