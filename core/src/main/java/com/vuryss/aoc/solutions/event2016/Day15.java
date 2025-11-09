package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Algorithm;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("unused")
public class Day15 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Disc #1 has 5 positions; at time=0, it is at position 4.
            Disc #2 has 2 positions; at time=0, it is at position 1.
            """,
            "5"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return solve(input, false);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return solve(input, true);
    }

    private ArrayList<Disk> parseDisks(String input) {
        var disks = new ArrayList<Disk>();

        for (var line: input.trim().split("\n")) {
            var numbers = StringUtil.ints(line);
            disks.add(new Disk(numbers.get(1), numbers.get(3)));
        }

        return disks;
    }

    private String solve(String input, boolean addDisk) {
        var disks = parseDisks(input);

        if (addDisk) {
            disks.add(new Disk(11, 0));
        }

        var moduli = disks.stream().mapToLong(d -> d.positions).toArray();
        var remainders = disks.stream().mapToLong(d -> (d.positions - d.position - disks.indexOf(d) - 1) % d.positions).toArray();

        return String.valueOf(Algorithm.chineseRemainderTheorem(remainders, moduli));

        // Brute force solution (works also very fast)
//        advanceTime:
//        for (var time = 0; true; time++) {
//            for (var i = 0; i < disks.size(); i++) {
//                var disk = disks.get(i);
//
//                if ((disk.position + time + i + 1) % disk.positions != 0) {
//                    continue advanceTime;
//                }
//            }
//
//            return String.valueOf(time);
//        }
    }

    private record Disk(int positions, int position) {}
}
