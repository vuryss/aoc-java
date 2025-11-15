package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Map;

@SuppressWarnings("unused")
public class Day19 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of("5", "3");
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of("5", "2", "29", "2");
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        // Explanation: https://www.youtube.com/watch?v=uCsD3ZGzMgE
        var elfCount = Integer.parseInt(input.trim());

        return String.valueOf((elfCount - Integer.highestOneBit(elfCount) << 1) + 1);

        // Brute force solution
        //return bruteForcePart1(input);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var elfCount = Integer.parseInt(input.trim());

        if (elfCount < 10) {
            return bruteForcePart2(input);
        }

        // Find the closest power of 3 that is less than the elf count
        var n = 0;
        while (Math.pow(3, n) < elfCount) {
            n++;
        }
        var start = (int) Math.pow(3, n - 1);
        var winner = elfCount - start;

        if (winner > start) {
            winner += (winner - start);
        }

        return String.valueOf(winner);

        // Brute force solution
        //return bruteForcePart2(input);
    }

    private String bruteForcePart1(String input) {
        var active = trainElves(input);

        while (active.next != active.previous) {
            active.eliminateNext();
            active = active.next;
        }

        return String.valueOf(active.index);
    }

    private String bruteForcePart2(String input) {
        var elfCount = Integer.parseInt(input.trim());
        Elf active = trainElves(input), pointer = active.findOpposite(elfCount);

        while (elfCount > 1) {
            pointer.previous.next = pointer.next;
            pointer.next.previous = pointer.previous;
            pointer = pointer.next;

            elfCount--;

            if (elfCount % 2 == 0) {
                pointer = pointer.next;
            }

            active = active.next;
        }

        return String.valueOf(active.index);
    }

    private Elf trainElves(String input) {
        var elfCount = Integer.parseInt(input.trim());
        Elf firstElf = new Elf(1, null);
        Elf elf1 = firstElf, elf2 = null;

        for (var i = 2; i <= elfCount; i++) {
            elf2 = new Elf(i, elf1);
            elf1.next = elf2;
            elf1 = elf2;
        }

        elf1.next = firstElf;
        firstElf.previous = elf1;

        return firstElf;
    }

    private static class Elf {
        public int index;
        public Elf next;
        public Elf previous;

        public Elf(int index, Elf previous) {
            this.index = index;
            this.previous = previous;
        }

        public void eliminateNext() {
            next = next.next;
            next.previous = this;
        }

        public Elf findOpposite(int elfCount) {
            var steps = elfCount / 2;
            var elf = this;

            while (steps-- > 0) {
                elf = elf.next;
            }

            return elf;
        }
    }
}
