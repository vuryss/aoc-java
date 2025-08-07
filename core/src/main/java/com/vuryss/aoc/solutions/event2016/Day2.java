package com.vuryss.aoc.solutions.event2016;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.CompassDirection;
import com.vuryss.aoc.util.Direction;
import com.vuryss.aoc.util.Point;

import java.util.Map;

/**
 * --- Day 2: Bathroom Security ---
 * You arrive at Easter Bunny Headquarters under cover of darkness. However, you left in such a rush that you forgot to use the bathroom! Fancy office buildings like this one usually have keypad locks on their bathrooms, so you search the front desk for the code.
 *
 * "In order to improve security," the document you find says, "bathroom codes will no longer be written down. Instead, please memorize and follow the procedure below to access the bathrooms."
 *
 * The document goes on to explain that each button to be pressed can be found by starting on the previous button and moving to adjacent buttons on the keypad: U moves up, D moves down, L moves left, and R moves right. Each line of instructions corresponds to one button, starting at the previous button (or, for the first line, the "5" button); press whatever button you're on at the end of each line. If a move doesn't lead to a button, ignore it.
 *
 * You can't hold it much longer, so you decide to figure out the code as you walk to the bathroom. You picture a keypad like this:
 *
 * 1 2 3
 * 4 5 6
 * 7 8 9
 * Suppose your instructions are:
 *
 * ULL
 * RRDDD
 * LURDL
 * UUUUD
 * You start at "5" and move up (to "2"), left (to "1"), and left (you can't, and stay on "1"), so the first button is 1.
 * Starting from the previous button ("1"), you move right twice (to "3") and then down three times (stopping at "9" after two moves and ignoring the third), ending up with 9.
 * Continuing from "9", you move left, up, right, down, and left, ending with 8.
 * Finally, you move up four times (stopping at "2"), then down once, ending with 5.
 * So, in this example, the bathroom code is 1985.
 *
 * Your puzzle input is the instructions from the document you found at the front desk. What is the bathroom code?
 *
 * --- Part Two ---
 * You finally arrive at the bathroom (it's a several minute walk from the lobby so visitors can behold the many fancy conference rooms and water coolers on this floor) and go to punch in the code. Much to your bladder's dismay, the keypad is not at all like you imagined it. Instead, you are confronted with the result of hundreds of man-hours of bathroom-keypad-design meetings:
 *
 *     1
 *   2 3 4
 * 5 6 7 8 9
 *   A B C
 *     D
 * You still start at "5" and stop when you're at an edge, but given the same instructions as above, the outcome is very different:
 *
 * You start at "5" and don't move at all (up and left are both edges), ending at 5.
 * Continuing from "5", you move right twice and down three times (through "6", "7", "B", "D", "D"), ending at D.
 * Then, from "D", you move five more times (through "D", "B", "C", "C", "B"), ending at B.
 * Finally, after five more moves, you end at 3.
 * So, given the actual keypad layout, the code would be 5DB3.
 *
 * Using the same instructions in your puzzle input, what is the correct bathroom code?
 */

@SuppressWarnings("unused")
public class Day2 implements SolutionInterface {
    private final Map<Point, Character> keypad1 = Map.of(
        new Point(0, 0), '1',
        new Point(1, 0), '2',
        new Point(2, 0), '3',
        new Point(0, 1), '4',
        new Point(1, 1), '5',
        new Point(2, 1), '6',
        new Point(0, 2), '7',
        new Point(1, 2), '8',
        new Point(2, 2), '9'
    );

    private final Map<Point, Character> keypad2 = Map.ofEntries(
        Map.entry(new Point(2, 0), '1'),
        Map.entry(new Point(1, 1), '2'),
        Map.entry(new Point(2, 1), '3'),
        Map.entry(new Point(3, 1), '4'),
        Map.entry(new Point(0, 2), '5'),
        Map.entry(new Point(1, 2), '6'),
        Map.entry(new Point(2, 2), '7'),
        Map.entry(new Point(3, 2), '8'),
        Map.entry(new Point(4, 2), '9'),
        Map.entry(new Point(1, 3), 'A'),
        Map.entry(new Point(2, 3), 'B'),
        Map.entry(new Point(3, 3), 'C'),
        Map.entry(new Point(2, 4), 'D')
    );

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
                ULL
                RRDDD
                LURDL
                UUUUD
                """,
            "1985"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
                ULL
                RRDDD
                LURDL
                UUUUD
                """,
            "5DB3"
        );
    }

    @Override
    public String part1Solution(String input) {
        return solve(input, keypad1, new Point(1, 1));
    }

    @Override
    public String part2Solution(String input) {
        return solve(input, keypad2, new Point(0, 2));
    }

    public String solve(String input, Map<Point, Character> keypad, Point position) {
        var lines = input.trim().split("\n");
        var code = new StringBuilder();

        for (var line: lines) {
            for (var c: line.toCharArray()) {
                var newPosition = position.forwardFromDirection(Direction.fromChar(c));

                if (keypad.containsKey(newPosition)) {
                    position = newPosition;
                }
            }

            code.append(keypad.get(position));
        }

        return code.toString();
    }
}
