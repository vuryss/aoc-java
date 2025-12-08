package com.vuryss.aoc.solutions.event2018;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.CompassDirection;
import com.vuryss.aoc.util.Point;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day20 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            "^WNE$", "3",
            "^ENWWW(NEEE|SSE(EE|N))$", "10",
            "^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$", "18",
            "^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$", "23",
            "^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$", "31"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        input = input.trim();
        input = input.substring(1, input.length()-1);

        return mapRooms(input.toCharArray()).stream().mapToLong(Room::getDistance).max().getAsLong() + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        input = input.trim();
        input = input.substring(1, input.length()-1);

        var rooms = mapRooms(input.toCharArray());

        return String.valueOf(rooms.stream().filter(r -> r.getDistance() >= 1000).count());
    }

    private Collection<Room> mapRooms(char[] input) {
        var roomsByPosition = new HashMap<Point, Room>();
        var currentRoom = new Room(new Point(0, 0));
        var stack = new Stack<Room>();

        currentRoom.distance = 0;
        roomsByPosition.put(currentRoom.position, currentRoom);

        for (char c : input) {
            if (c == 'N' || c == 'E' || c == 'S' || c == 'W') {
                var nextPosition = currentRoom.position.goInDirection(CompassDirection.fromChar(c));
                Room nextRoom;

                if (roomsByPosition.containsKey(nextPosition)) {
                    nextRoom = roomsByPosition.get(nextPosition);
                } else {
                    nextRoom = new Room(nextPosition);
                    roomsByPosition.put(nextPosition, nextRoom);
                }

                currentRoom.adjacent.add(nextRoom);
                nextRoom.adjacent.add(currentRoom);
                currentRoom = nextRoom;
            } else if (c == '(') {
                stack.push(currentRoom);
            } else if (c == ')') {
                currentRoom = stack.pop();
            } else if (c == '|') {
                currentRoom = stack.peek();
            }
        }

        return roomsByPosition.values();
    }

    private static class Room {
        public Point position;
        public Integer distance = null;
        public Set<Room> adjacent = new HashSet<>();

        public Room(Point position) {
            this.position = position;
        }

        public Integer getDistance() {
            if (distance == null) {
                for (var adjacent: adjacent) {
                    var adjacentRoomDistance = adjacent.getDistance(this);

                    if (null != adjacentRoomDistance) {
                        adjacentRoomDistance++;

                        if (null == distance) {
                            distance = adjacentRoomDistance;
                        } else if (adjacentRoomDistance < distance) {
                            distance = adjacentRoomDistance;
                        }
                    }
                }
            }

            return distance;
        }

        public Integer getDistance(Room excluded) {
            if (distance == null) {
                for (var adjacent: adjacent) {
                    if (adjacent == excluded) {
                        continue;
                    }

                    var adjacentRoomDistance = adjacent.getDistance(this);

                    if (null != adjacentRoomDistance) {
                        adjacentRoomDistance++;

                        if (null == distance) {
                            distance = adjacentRoomDistance;
                        } else if (adjacentRoomDistance < distance) {
                            distance = adjacentRoomDistance;
                        }
                    }
                }
            }

            return distance;
        }
    }
}
