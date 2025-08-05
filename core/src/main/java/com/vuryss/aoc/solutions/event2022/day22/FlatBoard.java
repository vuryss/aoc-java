package com.vuryss.aoc.solutions.event2022.day22;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FlatBoard {
    public Map<Point, Boolean> map = new HashMap<>();
    public Map<Integer, Integer> rowLeftmost = new HashMap<>();
    public Map<Integer, Integer> rowRightmost = new HashMap<>();
    public Map<Integer, Integer> columnTopmost = new HashMap<>();
    public Map<Integer, Integer> columnBottommost = new HashMap<>();
    public Point playerPosition;

    public void addPoint(Point point, boolean isWall) {
        this.map.put(point, !isWall);

        // Calculate the leftmost and rightmost points for each row, so we can wrap around it
        this.rowLeftmost.put(
            point.y,
            Math.min(rowLeftmost.getOrDefault(point.y, Integer.MAX_VALUE), point.x)
        );
        this.rowRightmost.put(
            point.y,
            Math.max(rowRightmost.getOrDefault(point.y, Integer.MIN_VALUE), point.x)
        );

        // Calculate the topmost and bottommost points for each column, so we can wrap around it
        this.columnTopmost.put(
            point.x,
            Math.min(columnTopmost.getOrDefault(point.x, Integer.MAX_VALUE), point.y)
        );
        this.columnBottommost.put(
            point.x,
            Math.max(columnBottommost.getOrDefault(point.x, Integer.MIN_VALUE), point.y)
        );
    }

    public boolean contains(Point point) {
        return this.map.containsKey(point);
    }

    public boolean isFreeTile(Point point) {
        return this.map.get(point);
    }

    public void setPlayerPosition(Point playerPosition) {
        this.playerPosition = playerPosition;
    }

    public void moveUp(int steps) {
        while (steps > 0) {
            var newPosition = new Point(playerPosition.x, playerPosition.y - 1);

            if (this.map.containsKey(newPosition)) {
                if (this.isFreeTile(newPosition)) {
                    playerPosition.y--;
                } else {
                    break;
                }
            } else {
                newPosition = new Point(playerPosition.x, this.columnBottommost.get(playerPosition.x));

                if (this.isFreeTile(newPosition)) {
                    playerPosition.y = newPosition.y;
                } else {
                    break;
                }
            }

            steps--;
        }
    }

    public void moveDown(int steps) {
        while (steps > 0) {
            var newPosition = new Point(playerPosition.x, playerPosition.y + 1);

            if (this.map.containsKey(newPosition)) {
                if (this.isFreeTile(newPosition)) {
                    playerPosition.y++;
                } else {
                    break;
                }
            } else {
                newPosition = new Point(playerPosition.x, this.columnTopmost.get(playerPosition.x));

                if (this.isFreeTile(newPosition)) {
                    playerPosition.y = newPosition.y;
                } else {
                    break;
                }
            }

            steps--;
        }
    }

    public void moveLeft(int steps) {
        while (steps > 0) {
            var newPosition = new Point(playerPosition.x - 1, playerPosition.y);

            if (this.map.containsKey(newPosition)) {
                if (this.isFreeTile(newPosition)) {
                    playerPosition.x--;
                } else {
                    break;
                }
            } else {
                newPosition = new Point(this.rowRightmost.get(playerPosition.y), playerPosition.y);

                if (this.isFreeTile(newPosition)) {
                    playerPosition.x = newPosition.x;
                } else {
                    break;
                }
            }

            steps--;
        }
    }

    public void moveRight(int steps) {
        while (steps > 0) {
            var newPosition = new Point(playerPosition.x + 1, playerPosition.y);

            if (this.map.containsKey(newPosition)) {
                if (this.isFreeTile(newPosition)) {
                    playerPosition.x++;
                } else {
                    break;
                }
            } else {
                newPosition = new Point(this.rowLeftmost.get(playerPosition.y), playerPosition.y);

                if (this.isFreeTile(newPosition)) {
                    playerPosition.x = newPosition.x;
                } else {
                    break;
                }
            }

            steps--;
        }
    }
}
