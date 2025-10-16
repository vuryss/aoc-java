package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.IntUtil;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

@SuppressWarnings("unused")
public class Day16 implements SolutionInterface {
    static int minutes;
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
            Valve BB has flow rate=13; tunnels lead to valves CC, AA
            Valve CC has flow rate=2; tunnels lead to valves DD, BB
            Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
            Valve EE has flow rate=3; tunnels lead to valves FF, DD
            Valve FF has flow rate=0; tunnels lead to valves EE, GG
            Valve GG has flow rate=0; tunnels lead to valves FF, HH
            Valve HH has flow rate=22; tunnel leads to valve GG
            Valve II has flow rate=0; tunnels lead to valves AA, JJ
            Valve JJ has flow rate=21; tunnel leads to valve II
            """,
            "1651"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
            Valve BB has flow rate=13; tunnels lead to valves CC, AA
            Valve CC has flow rate=2; tunnels lead to valves DD, BB
            Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
            Valve EE has flow rate=3; tunnels lead to valves FF, DD
            Valve FF has flow rate=0; tunnels lead to valves EE, GG
            Valve GG has flow rate=0; tunnels lead to valves FF, HH
            Valve HH has flow rate=22; tunnel leads to valve GG
            Valve II has flow rate=0; tunnels lead to valves AA, JJ
            Valve JJ has flow rate=21; tunnel leads to valve II
            """,
            "1707"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var valves = constructValves(input);
        Day16.minutes = 30;

        return solveWithPlayers(List.of(new SinglePath(valves.get("AA"), 0, 0 ,0)));
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var valves = constructValves(input);
        Day16.minutes = 26;

        return solveWithPlayers(
            List.of(
                new SinglePath(valves.get("AA"), 0, 0 ,0),
                new SinglePath(valves.get("AA"), 0, 0 ,0)
            )
        );
    }

    private String solveWithPlayers(List<SinglePath> players) {
        var maxReleasedPressure = 0;
        var bestPerOpened = new HashMap<Integer, Integer>();
        var queue = new LinkedList<MultiPath>();
        queue.add(new MultiPath(players, new HashSet<>()));

        while (!queue.isEmpty()) {
            var multiPath = queue.poll();
            var hasChange = false;

            for (var singlePath: multiPath.paths) {
                for (var valveDistance: singlePath.valve.distanceToValves.entrySet()) {
                    var resultingMinutes = valveDistance.getValue() + singlePath.minutes + 1;
                    var nextValve = valveDistance.getKey();

                    if (
                        nextValve.rate == 0
                        || resultingMinutes > Day16.minutes - 1
                        || multiPath.openedValves.contains(nextValve)
                    ) {
                        continue;
                    }

                    hasChange = true;

                    var newOpened = new HashSet<>(multiPath.openedValves);
                    newOpened.add(nextValve);

                    var newSinglePaths = new ArrayList<>(multiPath.paths);
                    newSinglePaths.remove(singlePath);
                    newSinglePaths.add(new SinglePath(
                        nextValve,
                        resultingMinutes,
                        singlePath.rate + nextValve.rate,
                        singlePath.releasedPressure + (singlePath.rate * (resultingMinutes - singlePath.minutes))
                    ));
                    newSinglePaths.sort((a, b) -> a.minutes == b.minutes ? a.rate - b.rate : a.minutes - b.minutes);

                    var newMultiPath = new MultiPath(newSinglePaths, newOpened);

                    if (
                        !bestPerOpened.containsKey(newMultiPath.hash)
                        || bestPerOpened.get(newMultiPath.hash) < newMultiPath.totalPressure()
                    ) {
                        queue.add(newMultiPath);
                        bestPerOpened.put(newMultiPath.hash, newMultiPath.totalPressure());
                    }
                }

                if (hasChange) {
                    break;
                }
            }

            if (!hasChange) {
                maxReleasedPressure = Math.max(maxReleasedPressure, multiPath.totalPressure());
            }
        }

        return String.valueOf(maxReleasedPressure);
    }

    private HashMap<String, Valve> constructValves(String input) {
        var lines = input.split("\n");
        var valves = new HashMap<String, Valve>();

        for (var line: lines) {
            var valveId = line.substring(6, 8);
            var rate = IntUtil.parseUnsignedInteger(line);
            var nextValves = line.split(" tunnels? leads? to valves? ")[1].split(", ");
            var valve = new Valve(valveId, rate, nextValves, new HashMap<>());

            valves.put(valveId, valve);
        }

        for (var valve: valves.values()) {
            if (!valve.id.equals("AA") && valve.rate == 0) {
                continue;
            }

            cycle1:
            for (var valve2: valves.values()) {
                if (valve == valve2 || valve2.rate == 0 || valve.distanceToValves.containsKey(valve2)) {
                    continue;
                }

                var queue = new LinkedList<Triple<Valve, HashSet<Valve>, Integer>>();
                queue.add(new ImmutableTriple<>(valve, new HashSet<>(), 0));

                while (!queue.isEmpty()) {
                    var node = queue.poll();
                    var newVisited = new HashSet<>(node.getMiddle());
                    newVisited.add(node.getLeft());

                    for (var valveId: node.getLeft().connectedValves) {
                        if (node.getMiddle().contains(valves.get(valveId))) {
                            continue;
                        }

                        if (valveId.equals(valve2.id)) {
                            valve.distanceToValves.put(valve2, node.getRight() + 1);
                            valve2.distanceToValves.put(valve, node.getRight() + 1);
                            continue cycle1;
                        }

                        queue.add(new ImmutableTriple<>(valves.get(valveId), newVisited, node.getRight() + 1));
                    }

                }
            }
        }

        return valves;
    }
    record SinglePath(Valve valve, int minutes, int rate, int releasedPressure) {
        public int totalPressure() {
            return releasedPressure + rate * (Day16.minutes - minutes);
        }
    }

    record MultiPath(List<SinglePath> paths, HashSet<Valve> openedValves, int hash) {
        MultiPath(List<SinglePath> paths, HashSet<Valve> openedValves) {
            this(
                paths,
                openedValves,
                openedValves.hashCode() + paths.stream().mapToInt(p -> p.valve.id.hashCode()).sum()
            );
        }

        public int totalPressure() {
            return paths.stream().mapToInt(SinglePath::totalPressure).sum();
        }
    }

    record Valve(String id, int rate, String[] connectedValves, Map<Valve, Integer> distanceToValves) {
        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }
}
