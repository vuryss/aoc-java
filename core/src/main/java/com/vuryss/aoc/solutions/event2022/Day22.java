package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.solutions.event2022.day22.Cube;
import com.vuryss.aoc.solutions.event2022.day22.FlatBoard;
import com.vuryss.aoc.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@SuppressWarnings({"DataFlowIssue", "unused"})
public class Day22 implements SolutionInterface {
    final byte TEST_SIZE = 4;
    final byte INPUT_SIZE = 50;

    enum Direction {
        UP, RIGHT, DOWN, LEFT;

        public Direction turnRight() {
            return switch (this) { case UP -> RIGHT; case RIGHT -> DOWN; case DOWN -> LEFT; case LEFT -> UP; };
        }

        public Direction turnLeft() {
            return switch (this) { case UP -> LEFT; case RIGHT -> UP; case DOWN -> RIGHT; case LEFT -> DOWN; };
        }
    }

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
                    ...#
                    .#..
                    #...
                    ....
            ...#.......#
            ........#...
            ..#....#....
            ..........#.
                    ...#....
                    .....#..
                    .#......
                    ......#.
                        
            10R5L5R10L4R5L5
            """,
            "6032"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
                    ...#
                    .#..
                    #...
                    ....
            ...#.......#
            ........#...
            ..#....#....
            ..........#.
                    ...#....
                    .....#..
                    .#......
                    ......#.
                        
            10R5L5R10L4R5L5
            """,
            "5031"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var parts = input.split("\n\n");
        var flatBoard = new FlatBoard();
        var lineIndex = 1;

        for (var line: parts[0].split("\n")) {
            var columnIndex = 1;

            for (var c: line.toCharArray()) {
                if (c == '.') {
                    flatBoard.addPoint(new Point(columnIndex, lineIndex), false);
                } else if (c == '#') {
                    flatBoard.addPoint(new Point(columnIndex, lineIndex), true);
                }

                columnIndex++;
            }

            lineIndex++;
        }

        var position = new Point(flatBoard.rowLeftmost.get(1), 1);
        var facing = Direction.RIGHT;
        var moves = StringUtil.toCharacterQueue(parts[1].trim());

        flatBoard.setPlayerPosition(position);

        while (!moves.isEmpty()) {
            var move = moves.poll().toString();

            if (StringUtils.isNumeric(move)) {
                while (!moves.isEmpty() && StringUtils.isNumeric(moves.peek().toString())) {
                    move += moves.poll();
                }
            }

            if (StringUtils.isNumeric(move)) {
                int steps = Integer.parseInt(move);
                switch (facing) {
                    case UP -> flatBoard.moveUp(steps);
                    case DOWN -> flatBoard.moveDown(steps);
                    case LEFT -> flatBoard.moveLeft(steps);
                    case RIGHT -> flatBoard.moveRight(steps);
                }
            } else if (move.equals("R")) {
                facing = facing.turnRight();
            } else if (move.equals("L")) {
                facing = facing.turnLeft();
            }
        }

        var facingValue = Map.of(Direction.RIGHT, 0, Direction.DOWN, 1, Direction.LEFT, 2, Direction.UP, 3);

        return String.valueOf(1000 * position.y + 4 * position.x + facingValue.get(facing));
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var parts = input.split("\n\n");
        var grid = new HashMap<Point, Character>();
        Point start = null;
        Direction facing = Direction.RIGHT;
        byte size = 0;

        int lineIndex = 0;
        for (var line : parts[0].split("\n")) {
            for (var i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '#' || line.charAt(i) == '.') {
                    grid.put(new Point(i, lineIndex), line.charAt(i));

                    if (start == null) {
                        start = new Point(i, lineIndex);
                    }
                }
            }

            if (size == 0) {
                size = line.length() >= INPUT_SIZE ? INPUT_SIZE : TEST_SIZE;
            }

            lineIndex++;
        }

        var gridSectorStart = (Point) start.clone();
        var cube = new Cube(size);
        var comingFromStack = new Stack<Direction>(){{ push(Direction.UP); }};

        while (true) {
            var leftStartingPoint = new Point(gridSectorStart.x - size, gridSectorStart.y);
            var rightStartingPoint = new Point(gridSectorStart.x + size, gridSectorStart.y);
            var upStartingPoint = new Point(gridSectorStart.x, gridSectorStart.y - size);
            var downStartingPoint = new Point(gridSectorStart.x, gridSectorStart.y + size);

            if (
                comingFromStack.peek() != Direction.LEFT
                && grid.containsKey(leftStartingPoint)
                && !cube.getSide(Cube.Side.LEFT).isMapped
            ) {
                // Go left
                cube.rotateLeft();
                gridSectorStart = leftStartingPoint;
                comingFromStack.add(Direction.RIGHT);
                continue;
            }

            if (
                comingFromStack.peek() != Direction.DOWN
                && grid.containsKey(downStartingPoint)
                && !cube.getSide(Cube.Side.BOTTOM).isMapped
            ) {
                // Go down
                cube.rotateDown();
                gridSectorStart = downStartingPoint;
                comingFromStack.add(Direction.UP);
                continue;
            }

            if (
                comingFromStack.peek() != Direction.RIGHT
                && grid.containsKey(rightStartingPoint)
                && !cube.getSide(Cube.Side.RIGHT).isMapped
            ) {
                // Go right
                cube.rotateRight();
                gridSectorStart = rightStartingPoint;
                comingFromStack.add(Direction.LEFT);
                continue;
            }

            if (
                comingFromStack.peek() != Direction.UP
                && grid.containsKey(upStartingPoint)
                && !cube.getSide(Cube.Side.TOP).isMapped
            ) {
                // Go up
                cube.rotateUp();
                gridSectorStart = upStartingPoint;
                comingFromStack.add(Direction.DOWN);
                continue;
            }

            // Map current front side
            var cubeSide = cube.getSide(Cube.Side.FRONT);
            var cubeSideGridPoint = new Point(0, 0);

            for (var y = gridSectorStart.y; y < gridSectorStart.y + size; y++) {
                cubeSideGridPoint.x = 0;
                for (var x = gridSectorStart.x; x < gridSectorStart.x + size; x++) {
                    var gridPoint = new Point(x, y);

                    cubeSide.sectors[cubeSideGridPoint.y][cubeSideGridPoint.x].related2dPoint = gridPoint;

                    if (grid.get(gridPoint) == '#') {
                        cubeSide.sectors[cubeSideGridPoint.y][cubeSideGridPoint.x].isWall = true;
                    }

                    cubeSideGridPoint.x++;
                }
                cubeSideGridPoint.y++;
            }

            cubeSide.isMapped = true;

            if (cube.isMapped()) {
                break;
            }

            // Go back
            switch (comingFromStack.pop()) {
                case UP -> {
                    cube.rotateUp();
                    gridSectorStart = upStartingPoint;
                }
                case RIGHT -> {
                    cube.rotateRight();
                    gridSectorStart = rightStartingPoint;
                }
                case DOWN -> {
                    cube.rotateDown();
                    gridSectorStart = downStartingPoint;
                }
                case LEFT -> {
                    cube.rotateLeft();
                    gridSectorStart = leftStartingPoint;
                }
            }
        }

        cube.recordRotations();
        var cubeSide = cube.getSide(Cube.Side.FRONT);
        Point cubeGridPoint = new Point(0, 0);

        var moves = StringUtil.toCharacterQueue(parts[1].trim());

        while (!moves.isEmpty()) {
            var move = moves.poll().toString();

            if (StringUtils.isNumeric(move)) {
                while (!moves.isEmpty() && StringUtils.isNumeric(moves.peek().toString())) {
                    move += moves.poll();
                }
            }

            if (StringUtils.isNumeric(move)) {
                var steps = Integer.parseInt(move);
                switch (facing) {
                    case UP -> {
                        for (var i = 0; i < steps; i++) {
                            if (cubeGridPoint.y == 0) {
                                if (cube.getSide(Cube.Side.TOP).sectors[size - 1][cubeGridPoint.x].isWall) {
                                    // There is wall on the next step, break movement
                                    break;
                                }
                                cube.rotateUp();
                                cubeSide = cube.getSide(Cube.Side.FRONT);
                                cubeGridPoint.y = size - 1;
                                continue;
                            } else if (cubeSide.sectors[cubeGridPoint.y - 1][cubeGridPoint.x].isWall) {
                                // There is wall on the next step on the current side, break movement
                                break;
                            }
                            cubeGridPoint.y--;
                        }
                    }
                    case DOWN -> {
                        for (var i = 0; i < steps; i++) {
                            if (cubeGridPoint.y == size - 1) {
                                if (cube.getSide(Cube.Side.BOTTOM).sectors[0][cubeGridPoint.x].isWall) {
                                    // There is wall on the next step, break movement
                                    break;
                                }
                                cube.rotateDown();
                                cubeSide = cube.getSide(Cube.Side.FRONT);
                                cubeGridPoint.y = 0;
                                continue;
                            } else if (cubeSide.sectors[cubeGridPoint.y + 1][cubeGridPoint.x].isWall) {
                                // There is wall on the next step on the current side, break movement
                                break;
                            }
                            cubeGridPoint.y++;
                        }
                    }
                    case LEFT -> {
                        for (var i = 0; i < steps; i++) {
                            if (cubeGridPoint.x == 0) {
                                if (cube.getSide(Cube.Side.LEFT).sectors[cubeGridPoint.y][size - 1].isWall) {
                                    // There is wall on the next step, break movement
                                    break;
                                }
                                cube.rotateLeft();
                                cubeSide = cube.getSide(Cube.Side.FRONT);
                                cubeGridPoint.x = size - 1;
                                continue;
                            } else if (cubeSide.sectors[cubeGridPoint.y][cubeGridPoint.x - 1].isWall) {
                                // There is wall on the next step on the current side, break movement
                                break;
                            }
                            cubeGridPoint.x--;
                        }
                    }
                    case RIGHT -> {
                        for (var i = 0; i < steps; i++) {
                            if (cubeGridPoint.x == size - 1) {
                                if (cube.getSide(Cube.Side.RIGHT).sectors[cubeGridPoint.y][0].isWall) {
                                    // There is wall on the next step, break movement
                                    break;
                                }
                                cube.rotateRight();
                                cubeSide = cube.getSide(Cube.Side.FRONT);
                                cubeGridPoint.x = 0;
                                continue;
                            } else if (cubeSide.sectors[cubeGridPoint.y][cubeGridPoint.x + 1].isWall) {
                                // There is wall on the next step on the current side, break movement
                                break;
                            }
                            cubeGridPoint.x++;
                        }
                    }
                }
            } else if (move.equals("R")) {
                facing = facing.turnRight();
            } else if (move.equals("L")) {
                facing = facing.turnLeft();
            }
        }

        var position = cubeSide.sectors[cubeGridPoint.y][cubeGridPoint.x].related2dPoint;

        // Rotate the cube side to the original position then to the mapped position to get the rotation on the flat map
        var rotations = (cubeSide.rotationsDuringWalking - cubeSide.rotationsBeforeMapping) % 4;

        while (rotations > 0) {
            facing = facing.turnLeft();
            rotations--;
        }

        while (rotations < 0) {
            facing = facing.turnRight();
            rotations++;
        }

        var facingValue = Map.of(Direction.RIGHT, 0, Direction.DOWN, 1, Direction.LEFT, 2, Direction.UP, 3);

        return String.valueOf(1000 * (position.y + 1) + 4 * (position.x + 1) + facingValue.get(facing));
    }
}
