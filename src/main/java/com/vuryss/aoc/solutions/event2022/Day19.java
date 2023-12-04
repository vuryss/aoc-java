package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.Utils;
import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.ListUtil;

import java.util.*;

public class Day19 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
            Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
            """,
            "33"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
            Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
            """,
            "3472"
        );
    }

    @Override
    public String part1Solution(String input) {
        var blueprints = getBlueprints(input);

        var qualityLevel = 0;

        for (var blueprint: blueprints) {
            qualityLevel += blueprint.id * findMaxGeodeForBlueprint(blueprint, 24);
        }

        return String.valueOf(qualityLevel);
    }

    @Override
    public String part2Solution(String input) {
        var blueprints = getBlueprints(input);

        if (blueprints.size() > 2) {
            blueprints = blueprints.subList(0, 3);
        }

        var result = 1;

        for (var blueprint: blueprints) {
            result *= findMaxGeodeForBlueprint(blueprint, 32);
        }

        return String.valueOf(result);
    }

    private int findMaxGeodeForBlueprint(Blueprint blueprint, int minutes) {
        var maxGeode = 0;
        var bestMinutePerRobotDistribution = new HashMap<RobotsCount, Integer>();
        var bestGeodePerMinute = new HashMap<Integer, Integer>();
        var queue = new PriorityQueue<State>((a, b) -> b.minutes - a.minutes);
        queue.add(new State(0, new Inventory(0, 0, 0, 0), new RobotsCount(1, 0, 0, 0)));

        while (!queue.isEmpty()) {
            var state = queue.poll();

            if (state.minutes == minutes) {
                if (state.inventory.geode > maxGeode) {
                    maxGeode = state.inventory.geode;
                }
                continue;
            }

            for (Type type: Type.values()) {
                var newRobots = new RobotsCount(state.robots, type);
                var cost = blueprint.robotCost.get(type);

                if (
                    newRobots.ore > blueprint.max.ore
                    || newRobots.clay > blueprint.max.clay
                    || newRobots.obsidian > blueprint.max.obsidian
                    || (state.robots.clay == 0 && cost.clay > 0)
                    || (state.robots.obsidian == 0 && cost.obsidian > 0)
                ) {
                    continue;
                }
                var neededMinutes = (int) Math.ceil(Math.max(0, cost.ore - state.inventory.ore) / (double) state.robots.ore);

                if (state.robots.clay > 0 && cost.clay > 0) {
                    neededMinutes = Math.max(
                        neededMinutes,
                        (int) Math.ceil(Math.max(0, cost.clay - state.inventory.clay) / (double) state.robots.clay)
                    );
                }

                if (state.robots.obsidian > 0 && cost.obsidian > 0) {
                    neededMinutes = Math.max(
                        neededMinutes,
                        (int) Math.ceil(Math.max(0, cost.obsidian - state.inventory.obsidian) / (double) state.robots.obsidian)
                    );
                }

                neededMinutes++;

                if (state.minutes + neededMinutes > minutes) {
                    continue;
                }

                var newInventory = new Inventory(
                    state.inventory.ore - cost.ore + (state.robots.ore * neededMinutes),
                    state.inventory.clay - cost.clay + (state.robots.clay * neededMinutes),
                    state.inventory.obsidian - cost.obsidian + (state.robots.obsidian * neededMinutes),
                    state.inventory.geode + (state.robots.geode * neededMinutes)
                );

                var newState = new State(state.minutes + neededMinutes, newInventory, newRobots);

                if (
                    (
                        bestGeodePerMinute.containsKey(newState.minutes)
                        && bestGeodePerMinute.get(newState.minutes) > newState.robots.geode
                    ) || (
                        bestMinutePerRobotDistribution.containsKey(newState.robots)
                        && bestMinutePerRobotDistribution.get(newState.robots) < newState.minutes
                    )
                ) {
                    continue;
                }

                bestGeodePerMinute.put(newState.minutes, newState.robots.geode);
                bestMinutePerRobotDistribution.put(newState.robots, newState.minutes);

                queue.add(newState);
            }

            var totalGeode = state.inventory.geode + state.robots.geode * (minutes - state.minutes);

            if (totalGeode > maxGeode) {
                maxGeode = totalGeode;
            }
        }

        return maxGeode;
    }

    private List<Blueprint> getBlueprints(String input) {
        var blueprints = new ArrayList<Blueprint>();

        for (var line: input.trim().split("\n")) {
            var inputNum = ListUtil.extractUnsignedIntegers(line);
            blueprints.add(new Blueprint(
                inputNum.get(0),
                Map.of(
                    Type.ORE, new Cost(inputNum.get(1), 0, 0, 0),
                    Type.CLAY, new Cost(inputNum.get(2), 0, 0, 0),
                    Type.OBSIDIAN, new Cost(inputNum.get(3), inputNum.get(4), 0, 0),
                    Type.GEODE, new Cost(inputNum.get(5), 0, inputNum.get(6), 0)
                )
            ));
        }

        return blueprints;
    }

    enum Type {ORE, CLAY, OBSIDIAN, GEODE}

    static final class Blueprint {
        public int id;
        public Map<Type, Cost> robotCost;
        public Cost max;

        Blueprint(int id, Map<Type, Cost> robotCost) {
            this.id = id;
            this.robotCost = robotCost;
            this.max = new Cost(0, 0, 0, 0);

            for (var cost: robotCost.values()) {
                this.max.ore = Math.max(cost.ore, this.max.ore);
                this.max.clay = Math.max(cost.clay, this.max.clay);
                this.max.obsidian = Math.max(cost.obsidian, this.max.obsidian);
            }
        }
    }

    static final class State {
        public int minutes;
        public Inventory inventory;
        public RobotsCount robots;

        State(int minutes, Inventory inventory, RobotsCount robots) {
            this.minutes = minutes;
            this.inventory = inventory;
            this.robots = robots;
        }
    }

    static final class Cost {
        public int ore;
        public int clay;
        public int obsidian;
        public int geode;

        Cost(int ore, int clay, int obsidian, int geode) {
            this.ore = ore;
            this.clay = clay;
            this.obsidian = obsidian;
            this.geode = geode;
        }
    }

    static final class Inventory {
        public int ore;
        public int clay;
        public int obsidian;
        public int geode;

        Inventory(int ore, int clay, int obsidian, int geode) {
            this.ore = ore;
            this.clay = clay;
            this.obsidian = obsidian;
            this.geode = geode;
        }

        @Override
        public int hashCode() {
            return Objects.hash(ore, clay, obsidian, geode);
        }
    }

    static final class RobotsCount {
        public int ore;
        public int clay;
        public int obsidian;
        public int geode;

        RobotsCount(int ore, int clay, int obsidian, int geode) {
            this.ore = ore;
            this.clay = clay;
            this.obsidian = obsidian;
            this.geode = geode;
        }

        public RobotsCount(RobotsCount base, Type type) {
            this(
                base.ore + (type == Type.ORE ? 1 : 0),
                base.clay + (type == Type.CLAY ? 1 : 0),
                base.obsidian + (type == Type.OBSIDIAN ? 1 : 0),
                base.geode + (type == Type.GEODE ? 1 : 0)
            );
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof RobotsCount rc) {
                return this.ore == rc.ore &&
                    this.clay == rc.clay &&
                    this.obsidian == rc.obsidian &&
                    this.geode == rc.geode;
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(ore, clay, obsidian, geode);
        }
    }
}
