package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;

import java.util.*;

@SuppressWarnings("unused")
public class Day23 implements SolutionInterface {
    enum Direction {
        NORTH, EAST, SOUTH, WEST;

        Direction next() {
            return switch (this) { case NORTH -> SOUTH; case EAST -> NORTH; case SOUTH -> WEST; case WEST -> EAST; };
        }
    }

    Map<Point, List<Direction>> surroundings = new HashMap<>(){{
        put(new Point(-1, -1), List.of(Direction.NORTH, Direction.WEST));
        put(new Point(0, -1), List.of(Direction.NORTH));
        put(new Point(1, -1), List.of(Direction.NORTH, Direction.EAST));
        put(new Point(-1, 0), List.of(Direction.WEST));
        put(new Point(1, 0), List.of(Direction.EAST));
        put(new Point(-1, 1), List.of(Direction.SOUTH, Direction.WEST));
        put(new Point(0, 1), List.of(Direction.SOUTH));
        put(new Point(1, 1), List.of(Direction.SOUTH, Direction.EAST));
    }};

    Map<Direction, Point> moveDelta = new HashMap<>(){{
        put(Direction.NORTH, new Point(0, -1));
        put(Direction.EAST, new Point(1, 0));
        put(Direction.SOUTH, new Point(0, 1));
        put(Direction.WEST, new Point(-1, 0));
    }};

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            ....#..
            ..###.#
            #...#.#
            .#...##
            #.###..
            ##.#.##
            .#..#..
            """,
            "110"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            ....#..
            ..###.#
            #...#.#
            .#...##
            #.###..
            ##.#.##
            .#..#..
            """,
            "20"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var elves = parseElvesLocations(input);

        organizeElves(elves, 10);

        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

        for (var elf: elves) {
            minX = Math.min(minX, elf.location.x);
            maxX = Math.max(maxX, elf.location.x);
            minY = Math.min(minY, elf.location.y);
            maxY = Math.max(maxY, elf.location.y);
        }

        return String.valueOf(
            ((maxX - minX + 1) * (maxY - minY + 1)) - elves.size()
        );
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var elves = parseElvesLocations(input);
        var round = organizeElves(elves, null);

        return String.valueOf(round);
    }

    List<Elf> parseElvesLocations(String input) {
        var elves = new ArrayList<Elf>();
        var lines = input.trim().split("\n");

        for (int y = 0; y < lines.length; y++) {
            var line = lines[y];

            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') {
                    elves.add(new Elf(new Point(x, y)));
                }
            }
        }

        return elves;
    }

    int organizeElves(List<Elf> elves, Integer limitRounds) {
        limitRounds = limitRounds == null ? Integer.MAX_VALUE : limitRounds;
        int round = 0;
        boolean elfMoved = true;
        var direction = Direction.NORTH;
        var occupiedLocations = getOccupiedLocations(elves);

        while (round < limitRounds && elfMoved) {
            var elvesPerLocation = new HashMap<Point, Integer>();
            elfMoved = false;

            processElves:
            for (var elf: elves) {
                boolean hasNearbyElves = false;

                var surroundingElves = new HashMap<>(Map.of(
                    Direction.NORTH, false,
                    Direction.EAST, false,
                    Direction.SOUTH, false,
                    Direction.WEST, false
                ));

                for (var surrounding: surroundings.entrySet()) {
                    var surroundingLocation = elf.location.add(surrounding.getKey());
                    if (occupiedLocations.contains(surroundingLocation)) {
                        for (var dir: surrounding.getValue()) {
                            surroundingElves.put(dir, true);
                            hasNearbyElves = true;
                        }
                    }
                }

                if (!hasNearbyElves) {
                    elf.desiredLocation = elf.location;
                    elvesPerLocation.put(elf.location, elvesPerLocation.getOrDefault(elf.location, 0) + 1);
                    continue;
                }

                var availableDirection = direction;

                while (surroundingElves.get(availableDirection)) {
                    availableDirection = availableDirection.next();

                    if (availableDirection == direction) {
                        elf.desiredLocation = elf.location;
                        elvesPerLocation.put(elf.location, elvesPerLocation.getOrDefault(elf.location, 0) + 1);
                        continue processElves;
                    }
                }

                var desiredLocation = elf.location.add(moveDelta.get(availableDirection));
                elf.desiredLocation = desiredLocation;
                elvesPerLocation.put(desiredLocation, elvesPerLocation.getOrDefault(desiredLocation, 0) + 1);
                elfMoved = true;
            }

            occupiedLocations.clear();

            for (var elf: elves) {
                if (elvesPerLocation.get(elf.desiredLocation) == 1) {
                    elf.location = elf.desiredLocation;
                }

                occupiedLocations.add(elf.location);
                elf.desiredLocation = null;
            }

            direction = direction.next();

            round++;
        }

        return round;
    }

    public Set<Point> getOccupiedLocations(List<Elf> elves) {
        var occupiedLocations = new HashSet<Point>();

        for (var elf: elves) {
            occupiedLocations.add(elf.location);
        }

        return occupiedLocations;
    }

    class Elf {
        Point location;
        Point desiredLocation;

        Elf (Point location) {
            this.location = location;
        }
    }
}
