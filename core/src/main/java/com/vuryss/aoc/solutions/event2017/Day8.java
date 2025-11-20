package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

@SuppressWarnings("unused")
public class Day8 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            b inc 5 if a > 1
            a inc 1 if b < 5
            c dec -10 if a >= 1
            c inc -20 if c == 10
            """,
            "1"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var registers = new HashMap<String, Integer>();
        BiFunction<String, Integer, Integer> increaseFn
            = (String register, Integer value) -> registers.compute(register, (k, v) -> v == null ? value : v + value);
        BiFunction<String, Integer, Integer> decreaseFn
            = (String register, Integer value) -> registers.compute(register, (k, v) -> v == null ? -value : v - value);

        for (var line: input.trim().split("\n")) {
            var parts = line.split(" ");
            var register = parts[0];
            var operation = Objects.equals(parts[1], "inc") ? increaseFn : decreaseFn;
            var value = Integer.parseInt(parts[2]);
            int conditionRegister = registers.compute(parts[4], (k, v) -> v == null ? 0 : v);
            var condition = parts[5];
            var conditionValue = Integer.parseInt(parts[6]);

            switch (condition) {
                case ">" -> { if (conditionRegister > conditionValue) operation.apply(register, value); }
                case "<" -> { if (conditionRegister < conditionValue) operation.apply(register, value); }
                case ">=" -> { if (conditionRegister >= conditionValue) operation.apply(register, value); }
                case "<=" -> { if (conditionRegister <= conditionValue) operation.apply(register, value); }
                case "==" -> { if (conditionRegister == conditionValue) operation.apply(register, value); }
                case "!=" -> { if (conditionRegister != conditionValue) operation.apply(register, value); }
            }
        }

        return String.valueOf(registers.values().stream().mapToInt(Integer::intValue).max().orElseThrow());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var registers = new HashMap<String, Integer>();
        var max = Integer.MIN_VALUE;
        BiFunction<String, Integer, Integer> increaseFn =
            (String register, Integer value) -> registers.compute(register, (k, v) -> v == null ? value : v + value);
        BiFunction<String, Integer, Integer> decreaseFn =
            (String register, Integer value) -> registers.compute(register, (k, v) -> v == null ? -value : v - value);

        for (var line: input.trim().split("\n")) {
            var parts = line.split(" ");
            var register = parts[0];
            var operation = Objects.equals(parts[1], "inc") ? increaseFn : decreaseFn;
            var value = Integer.parseInt(parts[2]);
            int conditionRegister = registers.compute(parts[4], (k, v) -> v == null ? 0 : v);
            var condition = parts[5];
            var conditionValue = Integer.parseInt(parts[6]);

            switch (condition) {
                case ">" -> { if (conditionRegister > conditionValue) operation.apply(register, value); }
                case "<" -> { if (conditionRegister < conditionValue) operation.apply(register, value); }
                case ">=" -> { if (conditionRegister >= conditionValue) operation.apply(register, value); }
                case "<=" -> { if (conditionRegister <= conditionValue) operation.apply(register, value); }
                case "==" -> { if (conditionRegister == conditionValue) operation.apply(register, value); }
                case "!=" -> { if (conditionRegister != conditionValue) operation.apply(register, value); }
            }

            max = Math.max(max, registers.getOrDefault(register, Integer.MIN_VALUE));
        }

        return String.valueOf(max);
    }
}
