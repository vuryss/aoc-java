package com.vuryss.aoc.solutions.event2023;

import com.vuryss.aoc.solutions.DayInterface;
import com.vuryss.aoc.util.Regex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public class Day15 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            HASH
            """,
            "52",
            """
            rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
            """,
            "1320"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
            """,
            "145"
        );
    }

    @Override
    public String part1Solution(String input) {
        var sum = 0;
        var steps = input.trim().split(",");

        for (var step: steps) {
            sum += step.chars().reduce(0, (a, b) -> (a + b) * 17 % 256);
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input) {
        var sum = 0;
        var steps = input.trim().split(",");
        var boxes = new ArrayList<Box>();

        for (var i = 0; i < 256; i++) {
            boxes.add(new Box(i));
        }

        nextStep:
        for (var step: steps) {
            var matches = Regex.matchNamedGroups("(?<name>[a-z]+)(?<sign>[=\\-])(?<value>\\d+)?", step);
            var lensName = matches.get("name");
            var boxIndex = lensName.chars().reduce(0, (a, b) -> (a + b) * 17 % 256);
            var box = boxes.get(boxIndex);

            if (matches.get("sign").charAt(0) == '=') {
                boolean hasLens = false;
                var num = Integer.parseInt(matches.get("value"));

                for (var lens: box.lenses) {
                    if (lens.name.equals(lensName)) {
                        lens.value = num;
                        continue nextStep;
                    }
                }

                box.lenses.add(new Lens(lensName, num));
                continue;
            }

            box.lenses.removeIf(lens -> lens.name.equals(lensName));
        }

        for (var box: boxes) {
            var lensIndex = 1;

            for (var lens: box.lenses) {
                sum += (1 + box.index) * lensIndex++ * lens.value;
            }
        }

        return String.valueOf(sum);
    }

    private static class Box {
        public int index;
        public List<Lens> lenses = new ArrayList<>();

        public Box(int index) {
            this.index = index;
        }
    }

    private static class Lens {
        public String name;
        public int value;

        public Lens(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }
}
