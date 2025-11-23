package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.DoublyLinkedList;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day17 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of("3", "638");
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var steps = Integer.parseInt(input.trim());
        var dll = new DoublyLinkedList<>(0);
        dll.next = dll;
        dll.previous = dll;

        for (var i = 1; i <= 2017; i++) {
            dll = dll.forward(steps);
            dll = dll.insertNext(i);
        }

        return String.valueOf(dll.next.value);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var steps = Integer.parseInt(input.trim());
        int position = 0;
        int valueAfterZero = 0;

        for (int size = 1; size <= 50000001; size++) {
            position = ((position + steps) % size) + 1;

            if (position == 1) {
                valueAfterZero = size;
            }
        }

        return String.valueOf(valueAfterZero);
    }
}
