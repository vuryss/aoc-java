package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day14 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
            Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
            """,
            "1120"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
            Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
            """,
            "689"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var reindeerMap = parseReindeer(input);
        var seconds = isTest ? 1000 : 2503;
        var maxDistance = 0;

        for (var reindeer: reindeerMap.values()) {
            int distance = seconds / (reindeer.flyTime + reindeer.restTime); // Number of full cycles
            distance *= reindeer.flyTime * reindeer.speed; // Times the distance covered in one full cycle
            distance += Math.min(seconds % (reindeer.flyTime + reindeer.restTime), reindeer.flyTime) * reindeer.speed; // Distance covered in partial cycle
            maxDistance = Math.max(maxDistance, distance);
        }

        return String.valueOf(maxDistance);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var reindeerMap = parseReindeer(input);
        var seconds = isTest ? 1000 : 2503;

        for (var i = 0; i < seconds; i++) {
            for (var reindeer: reindeerMap.values()) {
                reindeer.move();
            }

            var maxDistance = reindeerMap.values().stream().mapToInt(r -> r.distance).max().orElseThrow();
            reindeerMap.values().stream().filter(r -> r.distance == maxDistance).forEach(Reindeer::awardPoint);
        }

        return String.valueOf(reindeerMap.values().stream().mapToInt(r -> r.points).max().orElseThrow());
    }

    private HashMap<String, Reindeer> parseReindeer(String input) {
        var lines = input.trim().split("\n");
        var reindeerMap = new HashMap<String, Reindeer>();


        for (var line: lines) {
            var parts = Regex.matchGroups("^(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.", line);
            assert parts != null;
            reindeerMap.put(parts.getFirst(), new Reindeer(parts.getFirst(), Integer.parseInt(parts.get(1)), Integer.parseInt(parts.get(2)), Integer.parseInt(parts.get(3))));
        }

        return reindeerMap;
    }

    static class Reindeer {
        public String name;
        public int speed;
        public int flyTime;
        public int restTime;
        public int distance = 0;
        public int points = 0;
        public int passedTime = 0;

        public Reindeer(String name, int speed, int flyTime, int restTime) {
            this.name = name;
            this.speed = speed;
            this.flyTime = flyTime;
            this.restTime = restTime;
        }

        public void move() {
            if (passedTime % (flyTime + restTime) < flyTime) {
                distance += speed;
            }

            passedTime++;
        }

        public void awardPoint() {
            points++;
        }
    }
}
