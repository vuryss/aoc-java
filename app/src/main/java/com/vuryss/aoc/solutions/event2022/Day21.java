package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day21 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            root: pppw + sjmn
            dbpl: 5
            cczh: sllz + lgvd
            zczc: 2
            ptdq: humn - dvpt
            dvpt: 3
            lfqf: 4
            humn: 5
            ljgn: 2
            sjmn: drzm * dbpl
            sllz: 4
            pppw: cczh / lfqf
            lgvd: ljgn * ptdq
            drzm: hmdt - zczc
            hmdt: 32
            """,
            "152"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            root: pppw + sjmn
            dbpl: 5
            cczh: sllz + lgvd
            zczc: 2
            ptdq: humn - dvpt
            dvpt: 3
            lfqf: 4
            humn: 5
            ljgn: 2
            sjmn: drzm * dbpl
            sllz: 4
            pppw: cczh / lfqf
            lgvd: ljgn * ptdq
            drzm: hmdt - zczc
            hmdt: 32
            """,
            "301"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var entities = constructEntities(input, false);

        return String.valueOf(entities.get("root").result());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var entities = constructEntities(input, true);
        var root = (Group) entities.get("root");
        Entity groupWithUnknown;
        Long result;

        if (root.leftSide.isDetermined()) {
            result = root.leftSide.result();
            groupWithUnknown = root.rightSide;
        } else {
            result = root.rightSide.result();
            groupWithUnknown = root.leftSide;
        }

        while (true) {
            if (groupWithUnknown instanceof Group group) {
                group.simplify();

                if (group.leftSide.isDetermined()) {
                    groupWithUnknown = group.rightSide;

                    result = switch (group.operation) {
                        case '+' -> result - group.leftSide.result();
                        case '-' -> (result - group.leftSide.result()) * -1;
                        case '*' -> result / group.leftSide.result();
                        case '/' -> group.leftSide.result() / result;
                        default -> throw new RuntimeException("Unsupported operation");
                    };
                } else {
                    groupWithUnknown = group.leftSide;
                    result = switch (group.operation) {
                        case '+' -> result - group.rightSide.result();
                        case '-' -> result + group.rightSide.result();
                        case '*' -> result / group.rightSide.result();
                        case '/' -> result * group.rightSide.result();
                        default -> throw new RuntimeException("Unsupported operation");
                    };
                }
            } else if (groupWithUnknown instanceof Unknown) {
                return String.valueOf(result);
            }
        }
    }

    private Map<String, Entity> constructEntities(String input, boolean addUnknown) {
        var lines = input.trim().split("\n");
        var entities = new HashMap<String, Entity>();

        for (var line: lines) {
            var parts = line.split(" ");
            var name = parts[0].substring(0, parts[0].length() - 1);
            if (parts.length == 2) {
                entities.put(name, new Number(Long.parseLong(parts[1])));
            } else {
                entities.put(name, new Group(parts[1], parts[2].charAt(0), parts[3]));
            }
        }

        if (addUnknown) {
            entities.put("humn", new Unknown());
        }

        for (var set: entities.entrySet()) {
            if (set.getValue() instanceof Group group) {
                group.leftSide = entities.get(group.leftSideName);
                group.rightSide = entities.get(group.rightSideName);
            }
        }

        return entities;
    }

    interface Entity {
        boolean isDetermined();
        Long result();
    }

    static class Number implements Entity {
        public long number;

        public Number(long number) {
            this.number = number;
        }

        @Override
        public boolean isDetermined() {
            return true;
        }

        @Override
        public Long result() {
            return number;
        }
    }

    static class Unknown implements Entity {
        @Override
        public boolean isDetermined() {
            return false;
        }

        @Override
        public Long result() {
            throw new RuntimeException("Not determined");
        }
    }

    static class Group implements Entity {
        public String leftSideName;
        public Entity leftSide;
        public Character operation;
        public String rightSideName;
        public Entity rightSide;

        public Group(String leftSideName, Character operation, String rightSideName) {
            this.leftSideName = leftSideName;
            this.operation = operation;
            this.rightSideName = rightSideName;
        }

        public void simplify() {
            if (leftSide.isDetermined()) {
                leftSide = new Number(leftSide.result());
            } else if (leftSide instanceof Group group){
                group.simplify();
            }

            if (rightSide.isDetermined()) {
                rightSide = new Number(rightSide.result());
            } else if (rightSide instanceof Group group){
                group.simplify();
            }
        }

        @Override
        public boolean isDetermined() {
            return leftSide.isDetermined() && rightSide.isDetermined();
        }

        @Override
        public Long result() {
            return switch (operation) {
                case '+' -> leftSide.result() + rightSide.result();
                case '-' -> leftSide.result() - rightSide.result();
                case '*' -> leftSide.result() * rightSide.result();
                case '/' -> leftSide.result() / rightSide.result();
                default -> throw new RuntimeException("Unsupported operation");
            };
        }
    }
}
