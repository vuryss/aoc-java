package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.ArrayUtil;
import com.vuryss.aoc.util.Regex;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day4 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            [1518-11-01 00:00] Guard #10 begins shift
            [1518-11-01 00:05] falls asleep
            [1518-11-01 00:25] wakes up
            [1518-11-01 00:30] falls asleep
            [1518-11-01 00:55] wakes up
            [1518-11-01 23:58] Guard #99 begins shift
            [1518-11-02 00:40] falls asleep
            [1518-11-02 00:50] wakes up
            [1518-11-03 00:05] Guard #10 begins shift
            [1518-11-03 00:24] falls asleep
            [1518-11-03 00:29] wakes up
            [1518-11-04 00:02] Guard #99 begins shift
            [1518-11-04 00:36] falls asleep
            [1518-11-04 00:46] wakes up
            [1518-11-05 00:03] Guard #99 begins shift
            [1518-11-05 00:45] falls asleep
            [1518-11-05 00:55] wakes up
            """,
            "240"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            [1518-11-01 00:00] Guard #10 begins shift
            [1518-11-01 00:05] falls asleep
            [1518-11-01 00:25] wakes up
            [1518-11-01 00:30] falls asleep
            [1518-11-01 00:55] wakes up
            [1518-11-01 23:58] Guard #99 begins shift
            [1518-11-02 00:40] falls asleep
            [1518-11-02 00:50] wakes up
            [1518-11-03 00:05] Guard #10 begins shift
            [1518-11-03 00:24] falls asleep
            [1518-11-03 00:29] wakes up
            [1518-11-04 00:02] Guard #99 begins shift
            [1518-11-04 00:36] falls asleep
            [1518-11-04 00:46] wakes up
            [1518-11-05 00:03] Guard #99 begins shift
            [1518-11-05 00:45] falls asleep
            [1518-11-05 00:55] wakes up
            """,
            "4455"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var guardSleepTimes = getGuardSleepTimes(input);
        int guardWithMostSleep = 0, mostSleep = 0, minuteMostAsleep = 0;

        for (var entry: guardSleepTimes.entrySet()) {
            var sleepTime = entry.getValue().stream().mapToInt(s -> s.end - s.start).sum();

            if (sleepTime > mostSleep) {
                guardWithMostSleep = entry.getKey();
                mostSleep = sleepTime;

                var minuteSleepCount = new int[60];

                for (var sleep: entry.getValue()) {
                    for (var i = sleep.start; i < sleep.end; i++) {
                        minuteSleepCount[i]++;
                    }
                }

                minuteMostAsleep = ArrayUtil.maxItemIndex(minuteSleepCount);
            }
        }

        return String.valueOf(guardWithMostSleep * minuteMostAsleep);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var guardSleepTimes = getGuardSleepTimes(input);
        int guardWithMostSleep = 0, mostSleep = 0, minuteMostAsleep = 0;

        for (var entry: guardSleepTimes.entrySet()) {
            var sleepTimes = entry.getValue();
            var sleepPerMinute = new int[60];

            for (var sleepTime: sleepTimes) {
                for (var i = sleepTime.start; i < sleepTime.end; i++) {
                    sleepPerMinute[i]++;

                    if (sleepPerMinute[i] > mostSleep) {
                        guardWithMostSleep = entry.getKey();
                        mostSleep = sleepPerMinute[i];
                        minuteMostAsleep = i;
                    }
                }
            }
        }

        return String.valueOf(guardWithMostSleep * minuteMostAsleep);
    }

    private HashMap<Integer, ArrayList<SleepTime>> getGuardSleepTimes(String input) {
        var lines = Arrays.stream(input.trim().split("\n")).sorted().toList();
        var guardSleepTimes = new HashMap<Integer, ArrayList<SleepTime>>();
        int guardId = 0, startTime = 0;

        for (var line: lines) {
            if (line.contains("begins shift")) {
                guardId = Integer.parseInt(Objects.requireNonNull(Regex.matchGroups("#(\\d+)", line)).getFirst());
                continue;
            }

            if (line.contains("falls asleep")) {
                startTime = Integer.parseInt(Objects.requireNonNull(Regex.matchGroups(":([0-9]+)", line)).getFirst());
                continue;
            }

            if (line.contains("wakes up")) {
                var endTime = Integer.parseInt(Objects.requireNonNull(Regex.matchGroups(":([0-9]+)", line)).getFirst());
                guardSleepTimes.computeIfAbsent(guardId, k -> new ArrayList<>()).add(new SleepTime(startTime, endTime));
            }
        }

        return guardSleepTimes;
    }

    private record SleepTime(int start, int end) {}
}
