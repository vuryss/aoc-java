package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.Util;

import java.util.*;

@SuppressWarnings("unused")
public class Day11 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
            """,
            "1656"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
            """,
            "195"
        );
    }

    @Override
    public String part1Solution(String input) {
        var octopuses = buildGrid(input);
        var count = 0;

        for (var i = 0; i < 100; i++) {
            for (var octopus : octopuses.values()) octopus.increaseEnergy();

            for (var octopus : octopuses.values()) {
                if (octopus.flashed) {
                    count++;
                    octopus.flashed = false;
                    octopus.energy = 0;
                }
            }
        }

        return String.valueOf(count);
    }

    @Override
    public String part2Solution(String input) {
        var octopuses = buildGrid(input);
        var stepCount = 0;

        while (true) {
            for (var octopus : octopuses.values()) octopus.increaseEnergy();

            stepCount++;
            var flashedCount = 0;

            for (var octopus : octopuses.values()) {
                if (octopus.flashed) {
                    octopus.flashed = false;
                    octopus.energy = 0;
                    flashedCount++;
                }
            }

            if (flashedCount == 100) {
                return String.valueOf(stepCount);
            }
        }
    }

    private Map<Point, Octopus> buildGrid(String input) {
        var octopuses = new HashMap<Point, Octopus>();
        var grid = Util.inputToGrid(input);

        for (var entry : grid.entrySet()) {
            octopuses.put(entry.getKey(), new Octopus(Character.getNumericValue(entry.getValue()), entry.getKey()));
        }

        for (var entry : octopuses.entrySet()) {
            var octopus = entry.getValue();

            for (var point: octopus.location.surroundingPoints()) {
                if (octopuses.containsKey(point)) {
                    octopus.adjacent.add(octopuses.get(point));
                }
            }
        }

        return octopuses;
    }
}

class Octopus {
    public int energy;
    public boolean flashed;
    public Set<Octopus> adjacent;
    public Point location;

    public Octopus(int energy, Point location) {
        this.energy = energy;
        this.location = location;
        this.flashed = false;
        this.adjacent = new HashSet<>();
    }

    public void increaseEnergy() {
        this.energy++;

        if (this.energy > 9 && !this.flashed) {
            this.flashed = true;

            for (var adjacent: this.adjacent) {
                adjacent.increaseEnergy();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Octopus octopus = (Octopus) o;
        return location.equals(octopus.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }
}
