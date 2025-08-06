package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.util.*;

@SuppressWarnings("unused")
public class Day12 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
            """,
            "10",
            """
            dc-end
            HN-start
            start-kj
            dc-start
            dc-HN
            LN-dc
            HN-end
            kj-sa
            kj-HN
            kj-dc
            """,
            "19",
            """
            fs-end
            he-DX
            fs-he
            start-DX
            pj-DX
            end-zg
            zg-sl
            zg-pj
            pj-he
            RW-he
            fs-DX
            pj-RW
            zg-RW
            start-pj
            he-WI
            zg-he
            pj-fs
            start-RW
            """,
            "226"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
            """,
            "36",
            """
            dc-end
            HN-start
            start-kj
            dc-start
            dc-HN
            LN-dc
            HN-end
            kj-sa
            kj-HN
            kj-dc
            """,
            "103",
            """
            fs-end
            he-DX
            fs-he
            start-DX
            pj-DX
            end-zg
            zg-sl
            zg-pj
            pj-he
            RW-he
            fs-DX
            pj-RW
            zg-RW
            start-pj
            he-WI
            zg-he
            pj-fs
            start-RW
            """,
            "3509"
        );
    }

    @Override
    public String part1Solution(String input) {
        return calculatePaths(input, false);
    }

    @Override
    public String part2Solution(String input) {
        return calculatePaths(input, true);
    }

    private String calculatePaths(String input, boolean allowSmallCaveTwice) {
        var count = 0;
        var caves = new HashMap<String, Cave>();

        for (var line: input.split("\n")) {
            var parts = line.split("-");
            var cave1 = caves.getOrDefault(parts[0], new Cave(parts[0]));
            var cave2 = caves.getOrDefault(parts[1], new Cave(parts[1]));
            cave1.connections.add(cave2);
            cave2.connections.add(cave1);
            caves.put(parts[0], cave1);
            caves.put(parts[1], cave2);
        }

        var queue = new LinkedList<Player>();
        var player = new Player(caves.get("start"));
        player.visited.put(caves.get("start"), 1);
        queue.add(player);

        while (!queue.isEmpty()) {
            player = queue.poll();

            for (var cave: player.current.connections) {
                if (cave.name.equals("end")) {
                    count++;
                    continue;
                }

                if (!player.canVisit(cave, allowSmallCaveTwice)) {
                    continue;
                }

                var newPlayer = new Player(cave, new HashMap<>(player.visited));
                newPlayer.addVisited(cave);
                queue.add(newPlayer);
            }
        }

        return String.valueOf(count);
    }
}

class Player {
    public Map<Cave, Integer> visited = new HashMap<>();
    public boolean visitedSmallCaveTwice = false;
    public Cave current;

    public Player(Cave current) {
        this.current = current;
    }

    public Player(Cave current, Map<Cave, Integer> visited) {
        this.current = current;
        this.visited = visited;
    }

    public void addVisited(Cave cave) {
        visited.put(cave, visited.getOrDefault(cave, 0) + 1);
        visitedSmallCaveTwice = hasVisitedSmallCaveTwice();
    }

    public boolean canVisit(Cave cave, boolean allowTwice) {
        if (!cave.isSmall) {
            return true;
        }

        if (cave.name.equals("start")) {
            return false;
        }

        if (!allowTwice || visitedSmallCaveTwice) {
            return visited.getOrDefault(cave, 0) == 0;
        }

        return visited.getOrDefault(cave, 0) < 2;
    }

    public boolean hasVisitedSmallCaveTwice() {
        for (var entry: visited.entrySet()) {
            if (entry.getKey().isSmall && entry.getValue() > 1) {
                return true;
            }
        }

        return false;
    }
}

class Cave {
    public boolean isSmall;
    public String name;
    public Set<Cave> connections = new HashSet<>();

    public Cave(String name) {
        this.name = name;
        this.isSmall = name.toLowerCase().equals(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cave cave = (Cave) o;
        return Objects.equals(name, cave.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
