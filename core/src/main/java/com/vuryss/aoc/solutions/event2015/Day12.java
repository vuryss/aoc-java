package com.vuryss.aoc.solutions.event2015;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.util.Map;

@SuppressWarnings("unused")
public class Day12 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "[1,2,3]", "6",
            "{\"a\":2,\"b\":4}", "6",
            "[[[3]]]", "3",
            "{\"a\":{\"b\":4},\"c\":-1}", "3",
            "{\"a\":[-1,1]}", "0",
            "[-1,{\"a\":1}]", "0",
            "[]", "0",
            "{}", "0"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            "[1,2,3]", "6",
            "[1,{\"c\":\"red\",\"b\":2},3]", "4",
            "{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}", "0",
            "[1,\"red\",5]", "6"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var numbers = StringUtil.sints(input);

        return String.valueOf(numbers.stream().mapToInt(Integer::intValue).sum());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;

        try {
            jsonNode = mapper.readTree(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return String.valueOf(sumNumbers(jsonNode));
    }

    private int sumNumbers(JsonNode jsonNode) {
        return switch (jsonNode) {
            case NumericNode n -> n.asInt();

            case ArrayNode a -> a.valueStream().map(this::sumNumbers).mapToInt(Integer::intValue).sum();

            case ObjectNode o -> {
                int sum = 0;

                for (var node: o) {
                    if (node.isTextual() && node.asText().equals("red")) {
                        yield 0;
                    }

                    sum += sumNumbers(node);
                }

                yield sum;
            }

            default -> 0;
        };
    }
}
