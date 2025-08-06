package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.PointLong;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@SuppressWarnings("unused")
public class Day11 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
            """,
            "374"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
            """,
            "82000210"
        );
    }

    @Override
    public String part1Solution(String input) {
        return String.valueOf(calculateDistancesBetweenGalaxies(input, 2));
    }

    @Override
    public String part2Solution(String input) {
        return String.valueOf(calculateDistancesBetweenGalaxies(input, 1000000));
    }

    private long calculateDistancesBetweenGalaxies(String input, int multiplier) {
        var lines = input.trim().split("\n");
        var galaxies = new ArrayList<PointLong>();
        var index = 1;

        for (var y = 0; y < lines.length; y++) {
            for (var x = 0; x < lines[y].length(); x++) {
                if (lines[y].charAt(x) == '#') {
                    galaxies.add(new PointLong(x, y));
                }
            }
        }

        var xPositions = galaxies.stream().map(g -> g.x).collect(Collectors.toSet());
        var yPositions = galaxies.stream().map(g -> g.y).collect(Collectors.toSet());
        var emptyX = LongStream.rangeClosed(0, lines.length - 1).filter(y -> !xPositions.contains(y)).boxed().toList();
        var emptyY = LongStream.rangeClosed(0, lines.length - 1).filter(y -> !yPositions.contains(y)).boxed().toList();

        for (var galaxy: galaxies) {
            var deltaX = emptyX.stream().filter(emptyColumn -> galaxy.x > emptyColumn).count();
            var deltaY = emptyY.stream().filter(emptyLine -> galaxy.y > emptyLine).count();
            var newPosition = galaxy.add(new PointLong(deltaX * (multiplier - 1), deltaY * (multiplier - 1)));
            galaxy.x = newPosition.x;
            galaxy.y = newPosition.y;
        }

        var sum = 0L;

        for (var i = 0; i < galaxies.size() - 1; i++) {
            for (var j = i; j < galaxies.size(); j++) {
                sum += galaxies.get(i).manhattan(galaxies.get(j));
            }
        }

        return sum;
    }
}
