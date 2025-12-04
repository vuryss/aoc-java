package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import org.jetbrains.annotations.NotNull;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day15 implements SolutionInterface {
    private static final Comparator<Point> readingOrder = Comparator.comparingLong(Point::sortingKeyTopLeft);
    private static final Comparator<Unit> lowerHealthThenReadingOrder = Comparator
        .<Unit>comparingInt(u -> u.health)
        .thenComparingLong(u -> u.position.sortingKeyTopLeft());

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            #######
            #.G...#
            #...EG#
            #.#.#G#
            #..G#E#
            #.....#
            #######
            """,
            "27730",
            """
            #######
            #G..#E#
            #E#E.E#
            #G.##.#
            #...#E#
            #...E.#
            #######
            """,
            "36334",
            """
            #######
            #E..EG#
            #.#G.E#
            #E.##E#
            #G..#.#
            #..E#.#
            #######
            """,
            "39514",
            """
            #######
            #E.G#.#
            #.#G..#
            #G.#.G#
            #G..#.#
            #...E.#
            #######
            """,
            "27755",
            """
            #######
            #.E...#
            #.#..G#
            #.###.#
            #E#G#G#
            #...#G#
            #######
            """,
            "28944",
            """
            #########
            #G......#
            #.E.#...#
            #..##..G#
            #...##..#
            #...#...#
            #.G...G.#
            #.....G.#
            #########
            """,
            "18740"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            #######
            #.G...#
            #...EG#
            #.#.#G#
            #..G#E#
            #.....#
            #######
            """,
            "4988",
            """
            #######
            #E..EG#
            #.#G.E#
            #E.##E#
            #G..#.#
            #..E#.#
            #######
            """,
            "31284",
            """
            #######
            #E.G#.#
            #.#G..#
            #G.#.G#
            #G..#.#
            #...E.#
            #######
            """,
            "3478",
            """
            #######
            #.E...#
            #.#..G#
            #.###.#
            #E#G#G#
            #...#G#
            #######
            """,
            "6474",
            """
            #########
            #G......#
            #.E.#...#
            #..##..G#
            #...##..#
            #...#...#
            #.G...G.#
            #.....G.#
            #########
            """,
            "1140"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var data = parseInput(input);
        var combat = new Combat(data.grid, data.units);

        return String.valueOf(combat.simulate());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var data = parseInput(input);

        for (var elfAttack = 4; true; elfAttack++) {
            Unit[] unitsCopy = new Unit[data.units.length];
            char[][] gridCopy = new char[data.grid.length][data.grid[0].length];

            for (var y = 0; y < data.grid.length; y++) {
                gridCopy[y] = data.grid[y].clone();
            }

            for (var i = 0; i < data.units.length; i++) {
                unitsCopy[i] = new Unit(data.units[i].type, data.units[i].position);

                if (unitsCopy[i].type == Type.ELF) {
                    unitsCopy[i].attack = elfAttack;
                }
            }

            var combat = new Combat(gridCopy, unitsCopy);
            var outcome = combat.simulate();

            if (!combat.hasDeadElves()) {
                return String.valueOf(outcome);
            }
        }
    }

    private Input parseInput(String input) {
        String[] lines = input.trim().split("\n");
        char[][] grid = new char[lines.length][lines[0].length()];
        List<Unit> listOfUnits = new ArrayList<>();

        for (short y = 0; y < lines.length; y++) {
            for (short x = 0; x < lines[y].length(); x++) {
                char c = lines[y].charAt(x);

                if (c == 'E' || c == 'G') {
                    listOfUnits.add(new Unit(c == 'E' ? Type.ELF : Type.GOBLIN, new Point(x, y)));
                }

                grid[y][x] = c;
            }
        }

        return new Input(grid, listOfUnits.toArray(new Unit[0]));
    }

    private static class Combat {
        public char[][] grid;
        public Unit[] units;

        public Combat(char[][] grid, Unit[] units) {
            this.grid = grid;
            this.units = units;
        }

        public int simulate() {
            for (int round = 1; true; round++) {
                Arrays.sort(units);

                for (var unit: units) {
                    if (!unit.alive) {
                        continue;
                    }

                    if (!unit.seesAnyAliveTargets(units)) {
                        int totalHealth = 0;

                        for (var u: units) {
                            if (u.alive) {
                                totalHealth += u.health;
                            }
                        }

                        return (round - 1) * totalHealth;
                    }

                    var attackTarget = unit.findTargetToDirectlyAttack(units);

                    if (null != attackTarget) {
                        unit.attack(attackTarget, grid);
                        continue;
                    }

                    unit.moveToClosestTarget(grid, units);

                    attackTarget = unit.findTargetToDirectlyAttack(units);

                    if (null != attackTarget) {
                        unit.attack(attackTarget, grid);
                    }
                }
            }
        }

        public boolean hasDeadElves() {
            for (var unit: units) {
                if (unit.type == Type.ELF && !unit.alive) {
                    return true;
                }
            }

            return false;
        }
    }

    private enum Type {
        ELF('E'),
        GOBLIN('G');

        public final char symbol;

        Type(char symbol) {
            this.symbol = symbol;
        }

        public Type opposite() {
            return this == ELF ? GOBLIN : ELF;
        }
    }

    private static class Unit implements Comparable<Unit> {
        public Type type;
        public int health = 200;
        public int attack = 3;
        public Point position;
        public boolean alive = true;

        public Unit(Type type, Point position) {
            this.type = type;
            this.position = position;
        }

        public Unit findTargetToDirectlyAttack(Unit[] units) {
            TreeSet<Unit> attackTargets = new TreeSet<>(lowerHealthThenReadingOrder);

            for (var unit: units) {
                if (unit.alive && unit.type == type.opposite() && unit.position.manhattan(position) == 1) {
                    attackTargets.add(unit);
                }
            }

            return attackTargets.isEmpty() ? null : attackTargets.first();
        }

        public void attack(Unit target, char[][] grid) {
            target.health -= attack;

            if (target.health <= 0) {
                target.alive = false;
                grid[target.position.y][target.position.x] = '.';
            }
        }

        public void moveToClosestTarget(char[][] grid, Unit[] units) {
            // Find all targets reachable in the fewest steps
            var closestTargets = new ArrayList<Unit>(units.length);
            int minDistance = Integer.MAX_VALUE;
            LinkedList<StateSearch>  queue = new LinkedList<>() {{ add(new StateSearch(position, 0)); }};
            boolean[][] visited = new boolean[grid.length][grid[0].length];

            while (!queue.isEmpty()) {
                var state = queue.poll();
                var point = state.position;
                var steps = state.steps;

                if (steps > minDistance || visited[point.y][point.x]) {
                    continue;
                }

                visited[point.y][point.x] = true;

                for (var adjacentPoint: point.getAdjacentPoints()) {
                    if (visited[adjacentPoint.y][adjacentPoint.x]) {
                        continue;
                    }

                    if (grid[adjacentPoint.y][adjacentPoint.x] == '.') {
                        queue.add(new StateSearch(adjacentPoint, steps + 1));
                    } else if (grid[adjacentPoint.y][adjacentPoint.x] == type.opposite().symbol) {
                        minDistance = steps;

                        for (var unit: units) {
                            if (unit.position.equals(adjacentPoint)) {
                                closestTargets.add(unit);
                                break;
                            }
                        }
                    }
                }
            }

            if (minDistance == Integer.MAX_VALUE) {
                return;
            }

            closestTargets.sort(null);
            Unit closestTarget = closestTargets.getFirst();

            // Now we have the closest, we need to find the next step, one of the adjacent points
            HashSet<Point> adjacentPoints = new HashSet<>(position.getAdjacentPoints());
            ArrayList<Point> validPoints = new ArrayList<>();
            queue = new LinkedList<>() {{ add(new StateSearch(closestTarget.position, 0)); }};
            visited = new boolean[grid.length][grid[0].length];

            while (!queue.isEmpty()) {
                var state = queue.poll();
                var point = state.position;

                if (state.steps > minDistance || visited[point.y][point.x]) {
                    continue;
                }

                visited[point.y][point.x] = true;

                if (adjacentPoints.contains(point)) {
                    validPoints.add(point);
                    continue;
                }

                for (var adjacentPoint: point.getAdjacentPoints()) {
                    if (visited[adjacentPoint.y][adjacentPoint.x]) {
                        continue;
                    }

                    if (grid[adjacentPoint.y][adjacentPoint.x] == '.') {
                        queue.add(new StateSearch(adjacentPoint, state.steps + 1));
                    }
                }
            }

            validPoints.sort(readingOrder);
            grid[position.y][position.x] = '.';
            position = validPoints.getFirst();
            grid[position.y][position.x] = type.symbol;
        }

        public boolean seesAnyAliveTargets(Unit[] units) {
            for (var unit: units) {
                if (unit.alive && unit.type == type.opposite()) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public int compareTo(@NotNull Unit unit) {
            return Long.compare(position.sortingKeyTopLeft(), unit.position.sortingKeyTopLeft());
        }
    }

    private record StateSearch(Point position, int steps) {}
    private record Input(char[][] grid, Unit[] units) {}
}
