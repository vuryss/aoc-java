package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.DoublyLinkedList;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day23 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of("389125467", "67384529");
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of("389125467", "149245887792");
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var numbers = Arrays.stream(input.trim().split("")).mapToInt(Integer::parseInt).boxed().toList();
        var max = numbers.stream().mapToInt(Integer::intValue).max().orElseThrow();
        var map = new HashMap<Integer, DoublyLinkedList<Integer>>();
        var first = numbers.getFirst();
        var dll = new DoublyLinkedList<>(first);
        map.put(first, dll);

        for (var i = 1; i < numbers.size(); i++) {
            var next = numbers.get(i);
            dll = dll.insertNext(next);
            map.put(next, dll);
        }

        var current = dll.next;
        var removed = new DoublyLinkedList[3];
        int dst;

        for (var i = 0; i < 100; i++) {
            for (var j = 0; j < 3; j++) {
                removed[j] = current.removeNext();
            }
            dst = current.value;
            do {
                dst--;
                if (dst == 0) dst = max;
            } while (dst == (int) removed[0].value || dst == (int) removed[1].value || dst == (int) removed[2].value);
            var destination = map.get(dst);
            for (var j = 0; j < 3; j++) destination = destination.insertNext(removed[j]);
            current = current.next;
        }

        current = map.get(1).next;
        var result = new StringBuilder();
        while (current.value != 1) {
            result.append(current.value);
            current = current.next;
        }

        return result.toString();
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        input = input.trim();
        int[] nextOf = new int[1_000_001];
        int current = input.charAt(0) - '0';

        for (var i = 1; i < input.length(); i++) {
            var next = input.charAt(i) - '0';
            nextOf[current] = next;
            current = next;
        }

        for (int i = input.length() + 1; i <= 1_000_000; i++) {
            nextOf[current] = i;
            current = i;
        }

        nextOf[current] = input.charAt(0) - '0';
        current = nextOf[current];
        int dest;

        for (var i = 0; i < 10_000_000; i++) {
            int next1 = nextOf[current];
            int next2 = nextOf[next1];
            int next3 = nextOf[next2];

            dest = current;
            do if (--dest < 1) dest = 1_000_000; while (dest == next1 || dest == next2 || dest == next3);

            nextOf[current] = nextOf[next3];
            nextOf[next3] = nextOf[dest];
            nextOf[dest] = next1;
            current = nextOf[current];
        }

        long product = (long) nextOf[1] * nextOf[nextOf[1]];

        return product + "";
    }
}
