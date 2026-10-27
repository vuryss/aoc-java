package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Combinatorics;
import com.vuryss.aoc.util.Regex;

import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day11 implements SolutionInterface {
    private final static Pattern GENERATOR_PATTERN = Pattern.compile("(\\w+) generator");
    private final static Pattern MICROCHIP_PATTERN = Pattern.compile("(\\w+)-compatible microchip");
    private final HashMap<String, Integer> itemBit = new HashMap<>();

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
            The second floor contains a hydrogen generator.
            The third floor contains a lithium generator.
            The fourth floor contains nothing relevant.
            """,
            "11"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        itemBit.clear();
        var floors = parseInput(input);

        return solve(floors);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        itemBit.clear();
        var floors = parseInput(input);
        var firstFloor = floors.getFirst();
        var elerium = itemBit("elerium");
        var dilithium = itemBit("dilithium");
        floors.set(
            0,
            new Floor(firstFloor.microchips | elerium | dilithium, firstFloor.generators | elerium | dilithium)
        );

        return solve(floors);
    }

    private String solve(ArrayList<Floor> floors) {
        var elevatorFloor = 0;
        var totalItems = itemBit.size() * 2;

        var queue = new PriorityQueue<>(Comparator.comparingInt(State::steps));
        queue.add(new State(0, elevatorFloor, floors, 0));

        var visited = new HashSet<String>();

        while (!queue.isEmpty()) {
            var state = queue.poll();

            if (visited.contains(state.hash())) {
                continue;
            }

            visited.add(state.hash());

            if (state.floors.get(3).size() == totalItems) {
                return String.valueOf(state.steps);
            }

            var currentFloor = state.floors.get(state.elevatorFloor);
            var currentObjects = currentFloor.objects();

            for (var twoItems : Combinatorics.combinations(currentObjects, 2)) {
                var newCurrentFloor = new Floor(currentFloor.all ^ twoItems.get(0) ^ twoItems.get(1));

                if (!newCurrentFloor.isSafe()) {
                    continue;
                }

                if (state.elevatorFloor < 3) {
                    var newNextFloor = new Floor(state.floors.get(state.elevatorFloor + 1).all | twoItems.get(0) | twoItems.get(1));

                    if (newNextFloor.isSafe()) {
                        var newState = new State(
                            state.steps + 1,
                            state.elevatorFloor + 1,
                            new ArrayList<>(state.floors) {{
                                set(state.elevatorFloor, newCurrentFloor);
                                set(state.elevatorFloor + 1, newNextFloor);
                            }},
                            state.minFloor
                        );

                        if (!visited.contains(newState.hash())) {
                            queue.add(newState);
                        }
                    }
                }

                if (state.elevatorFloor > state.minFloor) {
                    var newPreviousFloor = new Floor(state.floors.get(state.elevatorFloor - 1).all | twoItems.get(0) | twoItems.get(1));

                    if (newPreviousFloor.isSafe()) {
                        var newState = new State(
                            state.steps + 1,
                            state.elevatorFloor - 1,
                            new ArrayList<>(state.floors) {{
                                set(state.elevatorFloor, newCurrentFloor);
                                set(state.elevatorFloor - 1, newPreviousFloor);
                            }},
                            state.minFloor
                        );

                        if (!visited.contains(newState.hash())) {
                            queue.add(newState);
                        }
                    }
                }
            }

            for (var item : currentObjects) {
                var newCurrentFloor = new Floor(currentFloor.all ^ item);

                if (!newCurrentFloor.isSafe()) {
                    continue;
                }

                if (state.elevatorFloor < 3) {
                    var newNextFloor = new Floor(state.floors.get(state.elevatorFloor + 1).all | item);

                    if (newNextFloor.isSafe()) {
                        var newState = new State(
                            state.steps + 1,
                            state.elevatorFloor + 1,
                            new ArrayList<>(state.floors) {{
                                set(state.elevatorFloor, newCurrentFloor);
                                set(state.elevatorFloor + 1, newNextFloor);
                            }},
                            state.minFloor
                        );

                        if (!visited.contains(newState.hash())) {
                            queue.add(newState);
                        }
                    }
                }

                if (state.elevatorFloor > state.minFloor) {
                    var newPreviousFloor = new Floor(state.floors.get(state.elevatorFloor - 1).all | item);

                    if (!newPreviousFloor.isSafe()) {
                        continue;
                    }

                    var newState = new State(
                        state.steps + 1,
                        state.elevatorFloor - 1,
                        new ArrayList<>(state.floors) {{
                            set(state.elevatorFloor, newCurrentFloor);
                            set(state.elevatorFloor - 1, newPreviousFloor);
                        }},
                        state.minFloor
                    );

                    if (!visited.contains(newState.hash())) {
                        queue.add(newState);
                    }
                }
            }
        }

        return "-no solution found-";
    }

    private ArrayList<Floor> parseInput(String input) {
        var lines = input.trim().split("\n");
        var floors = new ArrayList<Floor>();

        for (var line: lines) {
            var microchips = Regex.matchAllGroups(MICROCHIP_PATTERN, line).stream()
                    .mapToInt(matches -> itemBit(matches.getFirst()))
                    .reduce(0, (a, b) -> a | b);
            var generators = Regex.matchAllGroups(GENERATOR_PATTERN, line).stream()
                    .mapToInt(matches -> itemBit(matches.getFirst()))
                    .reduce(0, (a, b) -> a | b);
            floors.add(new Floor(microchips, generators));
        }

        return floors;
    }

    private int itemBit(String name) {
        return itemBit.computeIfAbsent(name, k -> (int) Math.pow(2, itemBit.size()));
    }

    private record Floor(int microchips, int generators, long all) {
        Floor(int microchips, int generators) {
            this(microchips, generators, (long) microchips << 20 | generators);
        }

        Floor(long items) {
            this((int) (items >> 20), (int) (items & 0xFFFFF), items);
        }

        public int size() {
            return Long.bitCount(all);
        }

        public boolean isSafe() {
            return generators == 0 || (microchips & generators) == microchips;
        }

        public ArrayList<Long> objects() {
            var list = new ArrayList<Long>();
            var itemsCopy = all;

            while (itemsCopy != 0) {
                var bit = Long.highestOneBit(itemsCopy);
                itemsCopy ^= bit;
                list.add(bit);
            }

            return list;
        }
    }

    private record State(int steps, int elevatorFloor, ArrayList<Floor> floors, int minFloor) {
        State(int steps, int elevatorFloor, ArrayList<Floor> floors, int minFloor) {
            this.steps = steps;
            this.elevatorFloor = elevatorFloor;
            this.floors = floors;
            this.minFloor = floors.get(minFloor).all == 0 ? minFloor + 1 : minFloor;
        }

        public String hash() {
            var hash = new StringBuilder("E").append(elevatorFloor);

            for (int i = 0; i < floors.size(); i++) {
                var floor = floors.get(i);

                hash.append("F").append(i)
                    .append("M").append(Integer.bitCount(floor.microchips))
                    .append("G").append(Integer.bitCount(floor.generators));
            }

            return hash.toString();
        }
    }
}
