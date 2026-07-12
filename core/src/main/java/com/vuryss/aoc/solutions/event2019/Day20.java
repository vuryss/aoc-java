package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day20 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
                     A          \s
                     A          \s
              #######.######### \s
              #######.........# \s
              #######.#######.# \s
              #######.#######.# \s
              #######.#######.# \s
              #####  B    ###.# \s
            BC...##  C    ###.# \s
              ##.##       ###.# \s
              ##...DE  F  ###.# \s
              #####    G  ###.# \s
              #########.#####.# \s
            DE..#######...###.# \s
              #.#########.###.# \s
            FG..#########.....# \s
              ###########.##### \s
                         Z      \s
                         Z      \s
            """,
            "23",
            """
                               A              \s
                               A              \s
              #################.############# \s
              #.#...#...................#.#.# \s
              #.#.#.###.###.###.#########.#.# \s
              #.#.#.......#...#.....#.#.#...# \s
              #.#########.###.#####.#.#.###.# \s
              #.............#.#.....#.......# \s
              ###.###########.###.#####.#.#.# \s
              #.....#        A   C    #.#.#.# \s
              #######        S   P    #####.# \s
              #.#...#                 #......VT
              #.#.#.#                 #.##### \s
              #...#.#               YN....#.# \s
              #.###.#                 #####.# \s
            DI....#.#                 #.....# \s
              #####.#                 #.###.# \s
            ZZ......#               QG....#..AS
              ###.###                 ####### \s
            JO..#.#.#                 #.....# \s
              #.#.#.#                 ###.#.# \s
              #...#..DI             BU....#..LF
              #####.#                 #.##### \s
            YN......#               VT..#....QG
              #.###.#                 #.###.# \s
              #.#...#                 #.....# \s
              ###.###    J L     J    #.#.### \s
              #.....#    O F     P    #.#...# \s
              #.###.#####.#.#####.#####.###.# \s
              #...#.#.#...#.....#.....#.#...# \s
              #.#####.###.###.#.#.#########.# \s
              #...#.#.....#...#.#.#.#.....#.# \s
              #.###.#####.###.###.#.#.####### \s
              #.#.........#...#.............# \s
              #########.###.###.############# \s
                       B   J   C              \s
                       U   P   P              \s
            """,
            "58"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
                         Z L X W       C                \s
                         Z P Q B       K                \s
              ###########.#.#.#.#######.############### \s
              #...#.......#.#.......#.#.......#.#.#...# \s
              ###.#.#.#.#.#.#.#.###.#.#.#######.#.#.### \s
              #.#...#.#.#...#.#.#...#...#...#.#.......# \s
              #.###.#######.###.###.#.###.###.#.####### \s
              #...#.......#.#...#...#.............#...# \s
              #.#########.#######.#.#######.#######.### \s
              #...#.#    F       R I       Z    #.#.#.# \s
              #.###.#    D       E C       H    #.#.#.# \s
              #.#...#                           #...#.# \s
              #.###.#                           #.###.# \s
              #.#....OA                       WB..#.#..ZH
              #.###.#                           #.#.#.# \s
            CJ......#                           #.....# \s
              #######                           ####### \s
              #.#....CK                         #......IC
              #.###.#                           #.###.# \s
              #.....#                           #...#.# \s
              ###.###                           #.#.#.# \s
            XF....#.#                         RF..#.#.# \s
              #####.#                           ####### \s
              #......CJ                       NM..#...# \s
              ###.#.#                           #.###.# \s
            RE....#.#                           #......RF
              ###.###        X   X       L      #.#.#.# \s
              #.....#        F   Q       P      #.#.#.# \s
              ###.###########.###.#######.#########.### \s
              #.....#...#.....#.......#...#.....#.#...# \s
              #####.#.###.#######.#######.###.###.#.#.# \s
              #.......#.......#.#.#.#.#...#...#...#.#.# \s
              #####.###.#####.#.#.#.#.###.###.#.###.### \s
              #.......#.....#.#...#...............#...# \s
              #############.#.#.###.################### \s
                           A O F   N                    \s
                           A A D   M                    \s
            """,
            "396"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return solve(input, false);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return solve(input, true);
    }

    private String solve(String input, boolean recursive) {
        var data = parseInput(input);
        var nodes = linkNodes(data);
        var queue = new PriorityQueue<>(Comparator.comparingInt(State2::steps));
        queue.add(new State2(data.start, 0, 0));

        while (!queue.isEmpty()) {
            var state = queue.poll();

            if (state.location.equals(data.end)) {
                if (state.level == 0) return state.steps + "";
                continue;
            }

            for (var edge: nodes.get(state.location)) {
                var newLevel = recursive ? state.level + edge.levelChange : 0;

                if (edge.distance == 1 || newLevel < 0) continue;

                queue.add(new State2(edge.target, state.steps + edge.distance, newLevel));
            }
        }

        return "-not-found-";
    }

    private Data parseInput(String input) {
        var grid = StringUtil.toCharGrid(input);
        var portMap = new HashMap<String, Port>();
        var portLocations = new HashMap<Point, Port>();

        for (var y = 0; y < grid.length; y++) {
            for (var x = 0; x < grid[y].length; x++) {
                if (Character.isAlphabetic(grid[y][x])) {
                    String portName;
                    Point location = new Point(x, y);
                    Point arrivalsLoc;

                    if (x > 0 && grid[y][x-1] == '.') { // Exit to the right
                        portName = String.valueOf(grid[y][x]) + grid[y][x+1];
                        arrivalsLoc = new Point(x-1, y);
                    } else if (x < grid[y].length - 1 && grid[y][x+1] == '.') { // Exit to the left
                        portName = String.valueOf(grid[y][x-1]) + grid[y][x];
                        arrivalsLoc = new Point(x+1, y);
                    } else if (y < grid.length - 1 && grid[y+1][x] == '.') { // Exit up
                        portName = String.valueOf(grid[y-1][x]) + grid[y][x];
                        arrivalsLoc = new Point(x, y+1);
                    } else if (y > 0 && grid[y-1][x] == '.') { // Exit down
                        portName = String.valueOf(grid[y][x]) + grid[y+1][x];
                        arrivalsLoc = new Point(x, y-1);
                    } else {
                        continue;
                    }

                    Objects.requireNonNull(portName, "port name is null");
                    Objects.requireNonNull(arrivalsLoc, "grid location is null");

                    if (portMap.containsKey(portName)) {
                        var targetPort = portMap.get(portName);
                        var port = new Port(location, arrivalsLoc, targetPort.arrivalLocation, isOuter(location, grid));
                        portLocations.put(location, port);
                        var port2 = new Port(targetPort.location, targetPort.arrivalLocation, arrivalsLoc, isOuter(targetPort.location, grid));
                        portLocations.put(targetPort.location, port2);
                        portMap.remove(portName);
                    } else {
                        var port = new Port(location, arrivalsLoc, null, isOuter(location, grid));
                        portMap.put(portName, port);
                    }
                }
            }
        }

        return new Data(portLocations, portMap.get("AA").arrivalLocation, portMap.get("ZZ").arrivalLocation, grid);
    }

    private Map<Point, List<Edge>> linkNodes(Data data) {
        var nodes = new HashMap<Point, List<Edge>>();
        var waiting = new ArrayList<Point>();
        var calculated = new HashSet<Point>();
        waiting.add(data.start);

        while (!waiting.isEmpty()) {
            var queue = new ArrayDeque<State>();
            var visited = new HashSet<Point>();
            var fromPoint = waiting.removeFirst();
            visited.add(fromPoint);
            queue.add(new State(fromPoint, 0));

            while (!queue.isEmpty()) {
                var state = queue.pollFirst();

                for (var next: state.location.getAdjacentPoints()) {
                    if (next.equals(data.end)) {
                        var edges = nodes.getOrDefault(fromPoint, new ArrayList<>());

                        edges.add(new Edge(data.end, state.steps + 1, 0));
                        nodes.put(fromPoint, edges);
                        continue;
                    }

                    if (data.grid[next.y][next.x] == '.' && !visited.contains(next)) {
                        visited.add(next);
                        queue.add(new State(next, state.steps + 1));
                        continue;
                    }

                    if (data.ports.containsKey(next)) {
                        var port = data.ports.get(next);
                        var portTarget = port.departureLocation;
                        var levelChange = port.outer ? -1 : 1;
                        var edges = nodes.getOrDefault(fromPoint, new ArrayList<>());

                        edges.add(new Edge(portTarget, state.steps + 1, levelChange));
                        nodes.put(fromPoint, edges);

                        if (!calculated.contains(port.departureLocation)) {
                            waiting.add(port.departureLocation);
                            calculated.add(port.departureLocation);
                        }
                    }
                }
            }
        }

        return nodes;
    }

    private boolean isOuter(Point location, char[][] grid) {
        return location.x == 1 || location.y == 1 || location.x == grid[0].length - 2 || location.y == grid.length - 2;
    }

    private record Data(Map<Point, Port> ports, Point start, Point end, char[][] grid) {}
    private record Port(
        Point location,
        Point arrivalLocation,
        Point departureLocation,
        boolean outer
    ) {}
    private record Edge(Point target, int distance, int levelChange) {}
    private record State(Point location, int steps) {}
    private record State2(Point location, int steps, int level) {}
}
