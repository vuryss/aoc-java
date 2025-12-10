package com.vuryss.aoc.solutions.event2025;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Variable;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day10 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
            [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
            [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
            """,
            "7"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
            [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
            [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
            """,
            "33"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var machines = parseMachines(input);
        long sum = 0;

        for (var machine: machines) {
            var model = new ExpressionsBasedModel();
            Variable[] presses = new Variable[machine.buttons.length];

            for (var i = 0; i < machine.buttons.length; i++) {
                presses[i] = model.addVariable("btn" + i).binary().weight(1);
            }

            for (var i = 0; i < machine.joltageRequirements.length; i++) {
                var expression = model.addExpression("req" + i).level(machine.indicatorLights[i] ? 1 : 0);

                for (var j = 0; j < machine.buttons.length; j++) {
                    for (var index: machine.buttons[j]) {
                        if (index == i) {
                            expression.set(presses[j], 1);
                        }
                    }
                }

                Variable z = model.addVariable("z_"+i).lower(0).integer(true);
                expression.set(z, -2);
            }

            sum += Math.round(model.minimise().getValue());
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var machines = parseMachines(input);
        long sum = 0;

        for (var machine: machines) {
            var model = new ExpressionsBasedModel();
            Variable[] presses = new Variable[machine.buttons.length];

            for (var i = 0; i < machine.buttons.length; i++) {
                presses[i] = model.addVariable("btn" + i).lower(0).upper(300).integer(true).weight(1);
            }

            for (var i = 0; i < machine.joltageRequirements.length; i++) {
                var expression = model.addExpression("req" + i).level(machine.joltageRequirements[i]);

                for (var j = 0; j < machine.buttons.length; j++) {
                    for (var index: machine.buttons[j]) {
                        if (index == i) {
                            expression.set(presses[j], 1);
                        }
                    }
                }
            }

            sum += Math.round(model.minimise().getValue());
        }

        return String.valueOf(sum);
    }

    private Machine[] parseMachines(String input) {
        var lines = input.trim().split("\n");
        var machines = new Machine[lines.length];

        for (var i = 0; i < lines.length; i++) {
            machines[i] = parseMachine(lines[i]);
        }

        return machines;
    }

    private Machine parseMachine(String line) {
        var parts = line.split(" ");
        var indicatorLights = parts[0].substring(1, parts[0].length() - 1).chars().mapToObj(c -> c == '#').toArray(Boolean[]::new);
        var buttons = new int[parts.length - 2][];
        var joltageRequirements = StringUtil.ints(parts[parts.length - 1]).stream().mapToInt(Integer::intValue).toArray();

        for (var i = 1; i < parts.length - 1; i++) {
            buttons[i - 1] = StringUtil.ints(parts[i]).stream().mapToInt(Integer::intValue).toArray();
        }

        return new Machine(indicatorLights, buttons, joltageRequirements);
    }

    record Machine(Boolean[] indicatorLights, int[][] buttons, int[] joltageRequirements) {}
}
