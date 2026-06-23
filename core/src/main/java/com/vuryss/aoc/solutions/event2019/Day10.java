package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.MathUtil;
import com.vuryss.aoc.util.PointLong;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day10 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            .#..#
            .....
            #####
            ....#
            ...##
            """,
            "8",
            """
            ......#.#.
            #..#.#....
            ..#######.
            .#.#.###..
            .#..#.....
            ..#....#.#
            #..#....#.
            .##.#..###
            ##...#..#.
            .#....####
            """,
            "33",
            """
            #.#...#.#.
            .###....#.
            .#....#...
            ##.#.#.#.#
            ....#.#.#.
            .##..###.#
            ..#...##..
            ..##....##
            ......#...
            .####.###.
            """,
            "35",

            """
            .#..#..###
            ####.###.#
            ....###.#.
            ..###.##.#
            ##.##.#.#.
            ....###..#
            ..#.#..#.#
            #..#.#.###
            .##...##.#
            .....#.#..
            """,
            "41",
            """
            .#..##.###...#######
            ##.############..##.
            .#.######.########.#
            .###.#######.####.#.
            #####.##.#.##.###.##
            ..#####..#.#########
            ####################
            #.####....###.#.#.##
            ##.#################
            #####.##.###..####..
            ..######..##.#######
            ####.##.####...##..#
            .#####..#.######.###
            ##...#.##########...
            #.##########.#######
            .####.#.###.###.#.##
            ....##.##.###..#####
            .#.#.###########.###
            #.#.#.#####.####.###
            ###.##.####.##.#..##
            """,
            "210"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            .#..##.###...#######
            ##.############..##.
            .#.######.########.#
            .###.#######.####.#.
            #####.##.#.##.###.##
            ..#####..#.#########
            ####################
            #.####....###.#.#.##
            ##.#################
            #####.##.###..####..
            ..######..##.#######
            ####.##.####...##..#
            .#####..#.######.###
            ##...#.##########...
            #.##########.#######
            .####.#.###.###.#.##
            ....##.##.###..#####
            .#.#.###########.###
            #.#.#.#####.####.###
            ###.##.####.##.#..##
            """,
            "802"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return findBestAsteroidForStation(constructAsteroids(input)).sees + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var asteroids = constructAsteroids(input);
        var asteroid = findBestAsteroidForStation(asteroids);

        return asteroid.calculate200thAsteroid(asteroids) + "";
    }

    private List<Asteroid> constructAsteroids(String input) {
        var asteroids = new ArrayList<Asteroid>();
        var lines = input.trim().split("\n");

        for (var y = 0; y < lines.length; y++) {
            for (var x = 0; x < lines[y].length(); x++) {
                if (lines[y].charAt(x) == '#') {
                    asteroids.add(new Asteroid(new PointLong(x, y)));
                }
            }
        }

        return asteroids;
    }

    private Asteroid findBestAsteroidForStation(List<Asteroid> asteroids) {
        Asteroid stationAsteroid = null;
        var max = 0;

        for (Asteroid asteroid : asteroids) {
            asteroid.calculateSees(asteroids);
            if (asteroid.sees > max) {
                stationAsteroid = asteroid;
                max = asteroid.sees;
            }
        }

        return stationAsteroid;
    }

    private static class Asteroid {
        public PointLong point;
        public int sees = 0;
        public SortedMap<Double, SortedMap<Long, PointLong>> angleToPoints = new TreeMap<>();

        public Asteroid(PointLong point) {
            this.point = point;
        }

        public void calculateSees(List<Asteroid> asteroids) {
            var uniquePoints = new HashSet<PointLong>();

            for (var asteroid : asteroids) {
                if (asteroid.point.equals(this.point)) continue;

                var distance = new PointLong(asteroid.point.x - this.point.x, asteroid.point.y - this.point.y);
                var gcd = MathUtil.gcd(distance.x, distance.y);

                uniquePoints.add(new PointLong(distance.x / gcd, distance.y / gcd));
            }

            sees = uniquePoints.size();
        }

        public long calculate200thAsteroid(List<Asteroid> asteroids) {
            for (var asteroid : asteroids) {
                if (asteroid.point.equals(this.point)) continue;

                double angle = Math.toDegrees(Math.atan2(
                    asteroid.point.x - this.point.x,
                    this.point.y - asteroid.point.y
                ));

                if (angle < 0) angle += 360;

                long distance = this.point.manhattan(asteroid.point);
                var distanceToPoints = angleToPoints.getOrDefault(angle, new TreeMap<>());

                distanceToPoints.put(distance, asteroid.point);
                angleToPoints.put(angle, distanceToPoints);
            }

            int counter = 0;

            while (true) {
                for (var distanceToPoints : angleToPoints.values()) {
                    if (distanceToPoints.isEmpty()) continue;
                    counter++;

                    if (counter == 200) {
                        var point = distanceToPoints.get(distanceToPoints.firstKey());
                        return point.x * 100 + point.y;
                    }

                    distanceToPoints.remove(distanceToPoints.firstKey());
                }
            }
        }
    }
}
