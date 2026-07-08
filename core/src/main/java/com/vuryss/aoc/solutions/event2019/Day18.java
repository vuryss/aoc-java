package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day18 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            #########
            #b.A.@.a#
            #########
            """,
            "8",
            """
            ########################
            #f.D.E.e.C.b.A.@.a.B.c.#
            ######################.#
            #d.....................#
            ########################
            """,
            "86",
            """
            ########################
            #...............b.C.D.f#
            #.######################
            #.....@.a.B.c.d.A.e.F.g#
            ########################
            """,
            "132",
            """
            #################
            #i.G..c...e..H.p#
            ########.########
            #j.A..b...f..D.o#
            ########@########
            #k.E..a...g..B.n#
            ########.########
            #l.F..d...h..C.m#
            #################
            """,
            "136",
            """
            ########################
            #@..............ac.GI.b#
            ###d#e#f################
            ###A#B#C################
            ###g#h#i################
            ########################
            """,
            "81"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            #######
            #a.#Cd#
            ##...##
            ##.@.##
            ##...##
            #cB#Ab#
            #######
            """,
            "8",
            """
            ###############
            #d.ABC.#.....a#
            ######...######
            ######.@.######
            ######...######
            #b.....#.....c#
            ###############
            """,
            "24",
            """
            #############
            #DcBa.#.GhKl#
            #.###...#I###
            #e#d#.@.#j#k#
            ###C#...###J#
            #fEbA.#.FgHi#
            #############
            """,
            "32",
            """
            #############
            #g#f.D#..h#l#
            #F###e#E###.#
            #dCba...BcIJ#
            #####.@.#####
            #nK.L...G...#
            #M###N#H###.#
            #o#m..#i#jk.#
            #############
            """,
            "72"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var grid = StringUtil.toCharGrid(input.trim());
        var keys = new HashMap<Integer, Node>();
        var startPosition = new Point(0, 0);

        for (var y = 0; y < grid.length; y++) {
            for (var x = 0; x < grid[y].length; x++) {
                var position = new Point(x, y);
                if (grid[y][x] >= 'a' && grid[y][x] <= 'z') {
                    keys.put(getBit(grid[y][x]), new Node(position, grid[y][x]));
                } else if (grid[y][x] == '@') {
                    startPosition = position;
                }
            }
        }

        for (var key: keys.values()) {
            key.buildPaths(grid);
        }

        var entrance = new Node(startPosition, '@');
        entrance.buildPaths(grid);

        var queue = new PriorityQueue<>(Comparator.comparingInt(StepPart1::steps));
        queue.add(new StepPart1(entrance, 0, 0));

        var min = Integer.MAX_VALUE;
        var foundSteps = new HashMap<SeenState, Integer>();
        var allKeys = (1 << keys.size()) - 1;

        while (!queue.isEmpty()) {
            var state = queue.poll();

            for (var next: state.node.doorsToKey.keySet()) {
                if ((state.keys & next) > 0) continue; // We already have this key
                if ((state.keys & state.node.doorsToKey.get(next)) != state.node.doorsToKey.get(next)) continue;  // We don't have all keys to get there

                var newKeys = state.keys | next;
                var newSteps = state.steps + state.node.stepsToKey.get(next);

                if (newSteps >= min) continue;

                if (newKeys == allKeys) {
                    min = newSteps;
                    continue;
                }

                var checkState = new SeenState(keys.get(next).position, newKeys);

                if (foundSteps.containsKey(checkState) && foundSteps.get(checkState) <= newSteps) {
                    continue;
                }

                foundSteps.put(checkState, newSteps);
                queue.add(new StepPart1(
                    keys.get(next),
                    newSteps,
                    newKeys
                ));
            }
        }

        return min + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var grid = StringUtil.toCharGrid(input.trim());
        var keys = new HashMap<Integer, Node>();
        var startPosition = new Point(0, 0);

        for (var y = 0; y < grid.length; y++) {
            for (var x = 0; x < grid[y].length; x++) {
                var position = new Point(x, y);
                if (grid[y][x] >= 'a' && grid[y][x] <= 'z') {
                    keys.put(getBit(grid[y][x]), new Node(position, grid[y][x]));
                } else if (grid[y][x] == '@') {
                    startPosition = position;
                }
            }
        }

        grid[startPosition.y-1][startPosition.x-1] = '@';
        grid[startPosition.y-1][startPosition.x] = '#';
        grid[startPosition.y-1][startPosition.x+1] = '@';
        grid[startPosition.y][startPosition.x-1] = '#';
        grid[startPosition.y][startPosition.x] = '#';
        grid[startPosition.y][startPosition.x+1] = '#';
        grid[startPosition.y+1][startPosition.x-1] = '@';
        grid[startPosition.y+1][startPosition.x] = '#';
        grid[startPosition.y+1][startPosition.x+1] = '@';

        for (var key: keys.values()) {
            key.buildPaths(grid);
        }

        var robotA = new Node(new Point(startPosition.x - 1, startPosition.y - 1), '@');
        var robotB = new Node(new Point(startPosition.x - 1, startPosition.y + 1), '@');
        var robotC = new Node(new Point(startPosition.x + 1, startPosition.y - 1), '@');
        var robotD = new Node(new Point(startPosition.x + 1, startPosition.y + 1), '@');

        robotA.buildPaths(grid);
        robotB.buildPaths(grid);
        robotC.buildPaths(grid);
        robotD.buildPaths(grid);

        var queue = new PriorityQueue<>(Comparator.comparingInt(StepPart2::steps));
        queue.add(new StepPart2(Map.of('A', robotA, 'B', robotB, 'C', robotC, 'D', robotD), 0, 0));

        var min = Integer.MAX_VALUE;
        var seenStates = new HashMap<SeenState2, Integer>();
        var allKeys = (1 << keys.size()) - 1;

        while (!queue.isEmpty()) {
            var step = queue.poll();

            for (var entrySet: step.nodes.entrySet()) {
                for (var next: entrySet.getValue().doorsToKey.keySet()) {
                    if ((step.keys & next) > 0) continue; // We already have this key
                    if ((step.keys & entrySet.getValue().doorsToKey.get(next)) != entrySet.getValue().doorsToKey.get(next)) continue;  // We don't have all keys to get there

                    var newKeys = step.keys | next;
                    var newSteps = step.steps + entrySet.getValue().stepsToKey.get(next);

                    if (newSteps >= min) continue;

                    if (newKeys == allKeys) {
                        min = newSteps;
                        continue;
                    }

                    var newNodes = new HashMap<>(step.nodes);
                    newNodes.put(entrySet.getKey(), keys.get(next));

                    var newStep = new StepPart2(newNodes, newSteps, newKeys);
                    var checkState = new SeenState2(newStep.points(), newKeys);

                    if (seenStates.containsKey(checkState) && seenStates.get(checkState) <= newSteps) {
                        continue;
                    }

                    seenStates.put(checkState, newSteps);
                    queue.add(newStep);
                }
            }
        }

        return min + "";
//        return "";
    }

    private class Node {
        public final Point position;
        public final char name;
        public HashMap<Integer, Integer> stepsToKey = new HashMap<>();
        public HashMap<Integer, Integer> doorsToKey = new HashMap<>();

        public Node(Point position, char name) {
            this.position =  position;
            this.name = name;
        }

        public void buildPaths(char[][] grid) {
            var visited = new HashSet<Point>();
            var queue = new LinkedList<State>();
            queue.addLast(new State(position, 0, 0));

            while (!queue.isEmpty()) {
                var state = queue.pollFirst();

                for (var next: state.position.adjacentTopLeftOrder()) {
                    if (
                        next.x < 0 || next.x >= grid[0].length
                        || next.y < 0 || next.y >= grid.length
                        || grid[next.y][next.x] == '#'
                        || visited.contains(next)
                    ) continue;

                    visited.add(next);
                    var item = grid[next.y][next.x];
                    var newSteps = state.steps + 1;

                    if (isDoor(item)) {
                        var newDoors = state.doorBits | getBit(item);
                        queue.addLast(new State(next, newSteps, newDoors));
                        continue;
                    }

                    if (isKey(item)) {
                        stepsToKey.putIfAbsent(getBit(item), newSteps);
                        doorsToKey.putIfAbsent(getBit(item), state.doorBits);
                        queue.addLast(new State(next, newSteps, state.doorBits));
                        continue;
                    }

                    queue.addLast(new State(next, newSteps, state.doorBits));
                }
            }
        }

        @Override
        public String toString() {
            return "Key[" + name + "]";
        }

        private record State(Point position, int steps, int doorBits) {}
    }

    private boolean isDoor(char c) {
        return c >= 'A' && c <= 'Z';
    }

    private boolean isKey(char c) {
        return c >= 'a' && c <= 'z';
    }

    private int getBit(char c) {
        return 1 << (c >= 'a' ? c - 'a' : c - 'A');
    }

    private record StepPart1(Node node, int steps, int keys) {}
    private record SeenState(Point position, int keys) {}
    private record StepPart2(Map<Character, Node> nodes, int steps, int keys) {
        public Set<Point> points() {
            var points = new HashSet<Point>();
            nodes.forEach((ch, node) -> points.add(node.position));
            return points;
        }
    }
    private record SeenState2(Set<Point> points, int keys) {}
}
