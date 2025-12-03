package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Direction;
import com.vuryss.aoc.util.Point;
import org.jetbrains.annotations.NotNull;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day13 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            /->-\\       \s
            |   |  /----\\
            | /-+--+-\\  |
            | | |  | v  |
            \\-+-/  \\-+--/
              \\------/  \s
            """,
            "7,3"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            />-<\\ \s
            |   | \s
            | /<+-\\
            | | | v
            \\>+</ |
              |   ^
              \\<->/
            """,
            "6,4"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        return crashCarts(input, false);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return crashCarts(input, true);
    }

    private String crashCarts(String input, boolean removeCrashed) {
        var lines = input.split("\n");
        var carts = new ArrayList<Cart>();
        char[][] grid = new char[lines.length][lines[0].length()];
        boolean[][] cartsOnGrid = new boolean[lines.length][lines[0].length()];

        for (var y = 0; y < lines.length; y++) {
            for (var x = 0; x < lines[y].length(); x++) {
                var c = lines[y].charAt(x);

                if (c == '^' || c == 'v' || c == '<' || c == '>') {
                    carts.add(new Cart(new Point(x, y), Direction.fromArrowChar(c), 0));
                    cartsOnGrid[y][x] = true;
                    c = c == '^' || c == 'v' ? '|' : '-';
                }

                grid[y][x] = c;
            }
        }

        while (true) {
            var newCarts = new ArrayList<Cart>();
            var removed = new HashSet<Point>();

            for (var cart: carts) {
                if (removed.contains(cart.position())) {
                    continue;
                }

                var newPosition = cart.position().goInDirection(cart.direction());

                if (cartsOnGrid[newPosition.y][newPosition.x]) {
                    if (!removeCrashed) {
                        return newPosition.x + "," + newPosition.y;
                    }

                    cartsOnGrid[cart.position().y][cart.position().x] = false;
                    cartsOnGrid[newPosition.y][newPosition.x] = false;
                    newCarts.removeIf(c -> c.position().equals(newPosition));
                    removed.add(newPosition);
                    continue;
                }

                cartsOnGrid[cart.position().y][cart.position().x] = false;
                cartsOnGrid[newPosition.y][newPosition.x] = true;

                var newDirection = cart.direction();
                var turns = cart.turns();

                if (grid[newPosition.y][newPosition.x] == '+') {
                    newDirection = switch (cart.turns() % 3) {
                        case 0 -> cart.direction().turnLeft();
                        case 1 -> cart.direction();
                        case 2 -> cart.direction().turnRight();
                        default -> throw new RuntimeException("Unknown turn");
                    };
                    turns++;
                } else if (grid[newPosition.y][newPosition.x] == '/') {
                    newDirection = switch (cart.direction()) {
                        case R -> Direction.U;
                        case L -> Direction.D;
                        case U -> Direction.R;
                        case D -> Direction.L;
                    };
                } else if (grid[newPosition.y][newPosition.x] == '\\') {
                    newDirection = switch (cart.direction()) {
                        case R -> Direction.D;
                        case L -> Direction.U;
                        case U -> Direction.L;
                        case D -> Direction.R;
                    };
                }

                newCarts.add(new Cart(newPosition, newDirection, turns));
            }

            carts = newCarts;
            carts.sort(null);

            if (carts.size() == 1) {
                var cp = carts.getFirst().position();

                return cp.x + "," + cp.y;
            }
        }
    }

    private record Cart(Point position, Direction direction, int turns) implements Comparable<Cart> {
        @Override
        public int compareTo(@NotNull Day13.Cart cart) {
            return Long.compare(position().sortingKeyTopLeft(), cart.position().sortingKeyTopLeft());
        }
    }
}
