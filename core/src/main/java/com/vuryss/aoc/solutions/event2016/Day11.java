package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;

import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day11 implements SolutionInterface {
    private final static Pattern GENERATOR_PATTERN = Pattern.compile("(\\w+) generator");
    private final static Pattern MICROCHIP_PATTERN = Pattern.compile("(\\w+)-compatible microchip");
    private final ItemRepository itemRepository = new ItemRepository();

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
        itemRepository.items.clear();
        var floors = parseInput(input);
        itemRepository.link();

        return solve(floors);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        itemRepository.items.clear();
        var floors = parseInput(input);
        var firstFloor = floors.getFirst();
        firstFloor.add(itemRepository.add(Type.GENERATOR, "elerium"));
        firstFloor.add(itemRepository.add(Type.MICROCHIP, "elerium"));
        firstFloor.add(itemRepository.add(Type.GENERATOR, "dilithium"));
        firstFloor.add(itemRepository.add(Type.MICROCHIP, "dilithium"));
        itemRepository.link();

        return solve(floors);
    }

    private String solve(ArrayList<Set<Item>> floors) {
        var elevatorFloor = 0;
        var totalItems = floors.stream().mapToInt(Set::size).sum();

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
            var up2 = false;

            if (state.elevatorFloor < 3) {
                upLoop:
                for (var item : currentFloor) {
                    for (var otherItem : currentFloor) {
                        if (otherItem == item) {
                            continue;
                        }

                        var newCurrentFloor = new HashSet<>(currentFloor) {{ remove(item); remove(otherItem); }};

                        if (!isSafe(newCurrentFloor)) {
                            continue;
                        }

                        var newNextFloor = new HashSet<>(state.floors.get(state.elevatorFloor + 1)) {{ add(item); add(otherItem); }};

                        if (isSafe(newNextFloor)) {
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
                                up2 = true;
                                continue upLoop;
                            }
                        }
                    }

                    // Up with 1 item only if we did not go up with 2 items
                    if (up2) {
                        continue;
                    }

                    var newCurrentFloor = new HashSet<>(currentFloor) {{ remove(item); }};

                    if (isSafe(newCurrentFloor)) {
                        var newNextFloor = new HashSet<>(state.floors.get(state.elevatorFloor + 1)) {{ add(item); }};

                        if (isSafe(newNextFloor)) {
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
                                break;
                            }
                        }
                    }
                }
            }

            // Go downstairs only if there are items left to move up there
            if (state.elevatorFloor > state.minFloor) {
                // Try moving 1 item down
                for (var item: currentFloor) {
                    var newCurrentFloor = new HashSet<>(currentFloor) {{ remove(item); }};

                    if (!isSafe(newCurrentFloor)) {
                        continue;
                    }

                    var newPreviousFloor = new HashSet<>(state.floors.get(state.elevatorFloor - 1)) {{ add(item); }};

                    if (!isSafe(newPreviousFloor)) {
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

                // Try moving 2 items down
                for (var item : currentFloor) {
                    for (var otherItem : currentFloor) {
                        if (otherItem == item) {
                            continue;
                        }

                        var newCurrentFloor = new HashSet<>(currentFloor) {{ remove(item); remove(otherItem); }};

                        if (!isSafe(newCurrentFloor)) {
                            continue;
                        }

                        var newPreviousFloor = new HashSet<>(state.floors.get(state.elevatorFloor - 1)) {{ add(item); add(otherItem); }};

                        if (isSafe(newPreviousFloor)) {
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
            }
        }

        return "-no solution found-";
    }

    private ArrayList<Set<Item>> parseInput(String input) {
        var lines = input.trim().split("\n");
        var floors = new ArrayList<Set<Item>>();

        for (var line: lines) {
            var items = new HashSet<Item>();
            Regex.matchAllGroups(GENERATOR_PATTERN, line)
                .forEach(generator -> items.add(itemRepository.add(Type.GENERATOR, generator.getFirst())));
            Regex.matchAllGroups(MICROCHIP_PATTERN, line)
                .forEach(microchip -> items.add(itemRepository.add(Type.MICROCHIP, microchip.getFirst())));
            floors.add(items);
        }

        return floors;
    }

    private boolean isSafe(Set<Item> items) {
        return items.stream().noneMatch(item -> item.type == Type.GENERATOR)
            || items.stream().noneMatch(item -> item.type == Type.MICROCHIP && !items.contains(item.relatedItem));
    }

    private record State(int steps, int elevatorFloor, ArrayList<Set<Item>> floors, int minFloor) {
        State(int steps, int elevatorFloor, ArrayList<Set<Item>> floors, int minFloor) {
            this.steps = steps;
            this.elevatorFloor = elevatorFloor;
            this.floors = floors;
            this.minFloor = calculateMinFloor(floors);
        }

        private static int calculateMinFloor(ArrayList<Set<Item>> floors) {
            for (int i = 0; i < floors.size(); i++) {
                if (!floors.get(i).isEmpty()) {
                    return i;
                }
            }
            return 3; // All items are on floor 4 (index 3)
        }

        public String hash() {
            var hash = new StringBuilder("E").append(elevatorFloor);

            for (int i = 0; i < floors.size(); i++) {
                int mCount = 0, gCount = 0;
                for (var item : floors.get(i)) {
                    if (item.type == Type.MICROCHIP) mCount++;
                    else gCount++;
                }
                hash.append("F").append(i).append("M").append(mCount).append("G").append(gCount);
            }

            return hash.toString();
        }
    }

    private enum Type { GENERATOR, MICROCHIP }

    private static class Item {
        public Type type;
        public String name;
        public Item relatedItem;

        public Item(Type type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    private static class ItemRepository {
        private final ArrayList<Item> items = new ArrayList<>();

        public Item add(Type type, String name) {
            var item = new Item(type, name);
            items.add(item);
            return item;
        }

        public void link() {
            items.forEach(item -> {
                if (item.relatedItem == null) {
                    items.stream()
                        .filter(other -> item.name.equals(other.name) && item.type != other.type)
                        .findFirst()
                        .ifPresent(other -> {
                            item.relatedItem = other;
                            other.relatedItem = item;
                        });
                }
            });
        }
    }
}
