package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;

import java.util.*;

@SuppressWarnings("unused")
public class Day9 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            London to Dublin = 464
            London to Belfast = 518
            Dublin to Belfast = 141
            """,
            "605"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            London to Dublin = 464
            London to Belfast = 518
            Dublin to Belfast = 141
            """,
            "982"
        );
    }

    @Override
    public String part1Solution(String input) {
        return String.valueOf(calculateDistances(input).first());
    }

    @Override
    public String part2Solution(String input) {
        return String.valueOf(calculateDistances(input).last());
    }

    private TreeSet<Integer> calculateDistances(String input) {
        var lines = input.trim().split("\n");
        var cities = new HashMap<String, City>();
        var queue = new LinkedList<Route>();
        var distances = new TreeSet<Integer>();

        for (var line: lines) {
            var matches = Regex.matchGroups("^(\\w+) to (\\w+) = (\\d+)$", line);
            cities.putIfAbsent(matches.get(0), new City());
            cities.putIfAbsent(matches.get(1), new City());
            cities.get(matches.get(0)).distances.put(cities.get(matches.get(1)), Integer.parseInt(matches.get(2)));
            cities.get(matches.get(1)).distances.put(cities.get(matches.get(0)), Integer.parseInt(matches.get(2)));
        }

        for (var city: cities.values()) {
            queue.add(new Route(city, 0, Set.of(city)));
        }

        while (!queue.isEmpty()) {
            var route = queue.removeFirst();

            if (route.visitedCities.size() == cities.size()) {
                distances.add(route.distance);
                continue;
            }

            for (var city: route.city.distances.keySet()) {
                if (route.visitedCities.contains(city)) {
                    continue;
                }

                var visited = new HashSet<>(route.visitedCities);
                visited.add(city);

                queue.add(new Route(city, route.distance + route.city.distances.get(city), visited));
            }
        }

        return distances;
    }

    private static class City {
        private final Map<City, Integer> distances = new HashMap<>();
    }

    private static class Route {
        public final City city;
        public final int distance;
        public Set<City> visitedCities;

        public Route(City city, int distance, Set<City> visitedCities) {
            this.city = city;
            this.distance = distance;
            this.visitedCities = visitedCities;
        }
    }
}
