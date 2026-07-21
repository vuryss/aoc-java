package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.CompassDirection;
import com.vuryss.aoc.util.Regex;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class Day25 implements SolutionInterface {
    private static final Set<String> DANGEROUS_ITEMS = Set.of(
        "escape pod", "giant electromagnet", "infinite loop", "molten lava", "photons"
    );

    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var game = new Game(input);

        collectAllItems(game, null);

        var exitDirection = goToSecurityCheckpoint(game, null);

        return playItemsCombinations(List.copyOf(game.inventory), 0, game, exitDirection);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        return "Merry Christmas!";
    }

    private void collectAllItems(Game game, CompassDirection from) {
        var room = game.room;

        for (var item: room.items) if (!DANGEROUS_ITEMS.contains(item)) game.takeItem(item);

        if (room.name.equals("Security Checkpoint")) return;

        for (var direction: room.directions) {
            if (direction.equals(from)) continue;
            game.move(direction);
            collectAllItems(game, direction.opposite());
            game.move(direction.opposite());
        }
    }

    private CompassDirection goToSecurityCheckpoint(Game game, CompassDirection from) {
        var room = game.room;

        for (var direction: room.directions) {
            if (direction.equals(from)) continue;
            if (room.name.equals("Security Checkpoint")) return direction;

            game.move(direction);

            var checkDirection = goToSecurityCheckpoint(game, direction.opposite());

            if (checkDirection != null) return checkDirection;

            game.move(direction.opposite());
        }

        return null;
    }

    private String playItemsCombinations(
        List<String> allItems,
        int nextIndex,
        Game game,
        CompassDirection checkDirection
    ) {
        game.move(checkDirection);

        // Expected droids are heavier, so our droid is too light. Dropping additional items cannot help.
        if (game.output.contains("heavier")) return null;

        // Expected droids are lighter, so our droid is too heavy. Try dropping additional items.
        if (game.output.contains("lighter")) {
            for (var i = nextIndex; i < allItems.size(); i++) {
                var item = allItems.get(i);
                game.dropItem(item);

                var result = playItemsCombinations(allItems, i + 1, game, checkDirection);
                if (result != null) return result;

                game.takeItem(item);
            }

            return null;
        }

        return StringUtil.ints(game.output).getFirst() + "";
    }

    private static class Game {
        private final IntcodeComputer computer;
        private Room room;
        private String output;
        private final List<String> inventory = new ArrayList<>();

        public Game(String input) {
            computer = new IntcodeComputer(input.trim());
            computer.start();
            parseRoom();
        }

        public void move(CompassDirection direction) {
            var command = switch (direction) {
                case N -> "north\n";
                case E -> "east\n";
                case S -> "south\n";
                case W -> "west\n";
                default -> throw new RuntimeException("Invalid direction: " + direction);
            };
            for (var ch: command.toCharArray()) computer.input(ch);
            parseRoom();
        }

        public void takeItem(String item) {
            var command = String.format("take %s\n", item);
            for (var ch: command.toCharArray()) computer.input(ch);
            inventory.add(item);
            getOutput();
        }

        public void dropItem(String item) {
            var command = String.format("drop %s\n", item);
            for (var ch: command.toCharArray()) computer.input(ch);
            inventory.remove(item);
            getOutput();
        }

        public void parseRoom() {
            var output = getOutput();

            if (output.contains("Command?")) {
                var name = Regex.matchFirstGroup("==\\s(.*)\\s==", output);
                var stuff = Regex.matchAll("-\\s.*?\n", output).stream()
                    .map(s -> s.replace("- ", "").trim())
                    .toArray(String[]::new);
                var directions = new ArrayList<CompassDirection>();
                var items = new ArrayList<String>();

                for (var s: stuff) {
                    var direction = CompassDirection.tryFromString(s);

                    if (direction != null) directions.add(direction);
                    else items.add(s);
                }

                room = new Room(directions, items, name);
            }
        }

        private String getOutput() {
            var sb = new StringBuilder();

            while (computer.isRunning() || computer.hasOutput()) {
                while (computer.hasOutput()) sb.append((char) computer.takeSingleOutput());
                if (computer.isWaitingForInput() && !computer.hasOutput()) break;
            }

            output = sb.toString();

            return output;
        }

        record Room(List<CompassDirection> directions, List<String> items, String name) {}
    }
}
