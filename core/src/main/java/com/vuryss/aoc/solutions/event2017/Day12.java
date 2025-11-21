package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day12 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            0 <-> 2
            1 <-> 1
            2 <-> 0, 3, 4
            3 <-> 2, 4
            4 <-> 2, 3, 6
            5 <-> 6
            6 <-> 4, 5
            """,
            "6"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            0 <-> 2
            1 <-> 1
            2 <-> 0, 3, 4
            3 <-> 2, 4
            4 <-> 2, 3, 6
            5 <-> 6
            6 <-> 4, 5
            """,
            "2"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var programs = parsePrograms(input);
        var count = 0;
        var visited = new HashSet<Program>();
        var queue = new LinkedList<Program>();
        queue.add(programs.get(0));

        while (!queue.isEmpty()) {
            var program = queue.poll();

            if (visited.contains(program)) {
                continue;
            }

            visited.add(program);
            count++;

            for (var link: program.links) {
                if (!visited.contains(link)) queue.add(link);
            }
        }

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var programs = parsePrograms(input);
        var count = 0;
        var accountedFor = new HashSet<Program>();

        for (var program: programs.values()) {
            if (accountedFor.contains(program)) {
                continue;
            }

            count++;

            var queue = new LinkedList<Program>();
            queue.add(program);

            while (!queue.isEmpty()) {
                var p = queue.poll();

                if (accountedFor.contains(p)) {
                    continue;
                }

                accountedFor.add(p);

                for (var link: p.links) {
                    if (!accountedFor.contains(link)) queue.add(link);
                }
            }
        }

        return String.valueOf(count);
    }

    private HashMap<Integer, Program> parsePrograms(String input) {
        var lines = input.trim().split("\n");
        var programs = new HashMap<Integer, Program>();

        for (var line: lines) {
            var parts = StringUtil.ints(line);
            var program = programs.computeIfAbsent(parts.getFirst(), Program::new);
            parts.subList(1, parts.size()).forEach(p -> program.link(programs.computeIfAbsent(p, Program::new)));
        }

        return programs;
    }

    private static class Program {
        public int id;
        public HashSet<Program> links = new HashSet<>();

        public Program(int id) {
            this.id = id;
        }

        public void link(Program program) {
            links.add(program);
            program.links.add(this);
        }
    }
}
