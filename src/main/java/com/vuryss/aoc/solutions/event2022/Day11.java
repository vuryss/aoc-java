package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.Utils;
import com.vuryss.aoc.solutions.DayInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day11 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Monkey 0:
              Starting items: 79, 98
              Operation: new = old * 19
              Test: divisible by 23
                If true: throw to monkey 2
                If false: throw to monkey 3
             
            Monkey 1:
              Starting items: 54, 65, 75, 74
              Operation: new = old + 6
              Test: divisible by 19
                If true: throw to monkey 2
                If false: throw to monkey 0
             
            Monkey 2:
              Starting items: 79, 60, 97
              Operation: new = old * old
              Test: divisible by 13
                If true: throw to monkey 1
                If false: throw to monkey 3
             
            Monkey 3:
              Starting items: 74
              Operation: new = old + 3
              Test: divisible by 17
                If true: throw to monkey 0
                If false: throw to monkey 1
            """,
            "10605"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Monkey 0:
              Starting items: 79, 98
              Operation: new = old * 19
              Test: divisible by 23
                If true: throw to monkey 2
                If false: throw to monkey 3
             
            Monkey 1:
              Starting items: 54, 65, 75, 74
              Operation: new = old + 6
              Test: divisible by 19
                If true: throw to monkey 2
                If false: throw to monkey 0
             
            Monkey 2:
              Starting items: 79, 60, 97
              Operation: new = old * old
              Test: divisible by 13
                If true: throw to monkey 1
                If false: throw to monkey 3
             
            Monkey 3:
              Starting items: 74
              Operation: new = old + 3
              Test: divisible by 17
                If true: throw to monkey 0
                If false: throw to monkey 1
            """,
            "2713310158"
        );
    }

    @Override
    public String part1Solution(String input) {
        var monkeys = initializeMonkeys(input);

        executeRounds(monkeys, 20, true);

        return getLevelOfMonkeyBusiness(monkeys);
    }

    @Override
    public String part2Solution(String input) {
        var monkeys = initializeMonkeys(input);

        executeRounds(monkeys, 10_000, false);

        return getLevelOfMonkeyBusiness(monkeys);
    }

    private Monkey[] initializeMonkeys(String input) {
        var monkeyInputs = input.trim().split("\n\n");
        var monkeys = new Monkey[monkeyInputs.length];

        for (var i = 0; i < monkeyInputs.length; i++) {
            var monkeyInputLines = monkeyInputs[i].split("\n");
            var items = Utils.toLongList(monkeyInputLines[1].trim().split(": ")[1].split(", "));
            var operation = monkeyInputLines[2].trim().split("new = old ")[1];
            var divisible = Integer.parseInt(monkeyInputLines[3].replaceAll("\\D", ""));
            var trueMonkey = Integer.parseInt(monkeyInputLines[4].replaceAll("\\D", ""));
            var falseMonkey = Integer.parseInt(monkeyInputLines[5].replaceAll("\\D", ""));

            monkeys[i] = new Monkey(items, operation, divisible, trueMonkey, falseMonkey);
        }

        return monkeys;
    }

    private void executeRounds(Monkey[] monkeys, int rounds, boolean reliefWorryLevel) {
        var prod = 1;

        for (var monkey: monkeys) {
            prod *= monkey.divisible;
        }

        for (var round = 1; round <= rounds; round++) {
            for (var monkey: monkeys) {
                for (var item: monkey.items) {
                    monkey.inspectedItems++;

                    var operationParameter = monkey.operationParameter.equals("old")
                        ? item
                        : Integer.parseInt(monkey.operationParameter);

                    switch (monkey.operation) {
                        case '+' -> item = item + operationParameter;
                        case '*' -> item = item * operationParameter;
                    }

                    if (reliefWorryLevel) {
                        item /= 3;
                    }

                    var target = item % monkey.divisible == 0 ? monkey.trueTarget : monkey.falseTarget;
                    monkeys[target].items.add(item % prod);
                }

                monkey.items = new ArrayList<>();
            }
        }
    }

    private String getLevelOfMonkeyBusiness(Monkey[] monkeys) {
        var maxInspections = new ArrayList<Long>();

        for (var monkey: monkeys) {
            maxInspections.add(monkey.inspectedItems);
        }

        maxInspections.sort((a, b) -> Long.compare(b, a));

        return String.valueOf(maxInspections.get(0) * maxInspections.get(1));
    }

    static class Monkey {
        public List<Long> items;
        public char operation;
        public String operationParameter;
        public int divisible;
        public int trueTarget;
        public int falseTarget;
        public Long inspectedItems = 0L;

        public Monkey(List<Long> items, String operation, int divisible, int trueTarget, int falseTarget) {
            this.items = items;
            this.operation = operation.charAt(0);
            this.operationParameter = operation.substring(2);
            this.divisible = divisible;
            this.trueTarget = trueTarget;
            this.falseTarget = falseTarget;
        }
    }
}
