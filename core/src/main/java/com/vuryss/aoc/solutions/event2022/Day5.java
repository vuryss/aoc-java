package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.DayInterface;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@SuppressWarnings("unused")
public class Day5 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
                [D]    
            [N] [C]    
            [Z] [M] [P]
             1   2   3
            
            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2
            """,
            "CMZ"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
                [D]    
            [N] [C]    
            [Z] [M] [P]
             1   2   3
            
            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2
            """,
            "MCD"
        );
    }

    @Override
    public String part1Solution(String input) {
        var parts = input.split("\n\n");
        var stacks = createCurrentState(parts[0]);

        for (var line: parts[1].split("\n")) {
            var moveParts = line.trim().split(" ");
            var originStack = stacks.get(Integer.parseInt(moveParts[3]));
            var targetStack = stacks.get(Integer.parseInt(moveParts[5]));

            for (var i = 0; i < Integer.parseInt(moveParts[1]); i++) {
                targetStack.addFirst(originStack.pollFirst());
            }
        }

        return getTopCrates(stacks);
    }

    @Override
    public String part2Solution(String input) {
        var parts = input.split("\n\n");
        var stacks = createCurrentState(parts[0]);

        for (var line: parts[1].split("\n")) {
            var moveParts = line.trim().split(" ");
            var originStack = stacks.get(Integer.parseInt(moveParts[3]));
            var targetStack = stacks.get(Integer.parseInt(moveParts[5]));
            var tempList = new LinkedList<Character>();

            for (var i = 0; i < Integer.parseInt(moveParts[1]); i++) {
                tempList.add(originStack.pollFirst());
            }

            while (!tempList.isEmpty()) {
                targetStack.addFirst(tempList.pollLast());
            }
        }

        return getTopCrates(stacks);
    }

    private HashMap<Integer, LinkedList<Character>> createCurrentState(String state) {
        var stacks = new HashMap<Integer, LinkedList<Character>>();

        for (var line: state.split("\n")) {
            var charIndex = 1;
            var stackId = 1;

            while (line.length() > charIndex) {
                var c = line.charAt(charIndex);
                var stack = stacks.getOrDefault(stackId, new LinkedList<>());
                stacks.putIfAbsent(stackId, stack);

                if (c >= 'A' && c <= 'Z') {
                    stack.add(c);
                }

                charIndex += 4;
                stackId++;
            }
        }

        return stacks;
    }

    private String getTopCrates(HashMap<Integer, LinkedList<Character>> stacks) {
        StringBuilder result = new StringBuilder();

        for (var entry: stacks.entrySet()) {
            result.append(entry.getValue().peek());
        }

        return result.toString();
    }
}
