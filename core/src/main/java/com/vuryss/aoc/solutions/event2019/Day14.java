package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day14 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            10 ORE => 10 A
            1 ORE => 1 B
            7 A, 1 B => 1 C
            7 A, 1 C => 1 D
            7 A, 1 D => 1 E
            7 A, 1 E => 1 FUEL
            """,
            "31",
            """
            9 ORE => 2 A
            8 ORE => 3 B
            7 ORE => 5 C
            3 A, 4 B => 1 AB
            5 B, 7 C => 1 BC
            4 C, 1 A => 1 CA
            2 AB, 3 BC, 4 CA => 1 FUEL
            """,
            "165",
            """
            157 ORE => 5 NZVS
            165 ORE => 6 DCFZ
            44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL
            12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ
            179 ORE => 7 PSHF
            177 ORE => 5 HKGWZ
            7 DCFZ, 7 PSHF => 2 XJWVT
            165 ORE => 2 GPVTF
            3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT
            """,
            "13312",
            """
            2 VPVL, 7 FWMGM, 2 CXFTF, 11 MNCFX => 1 STKFG
            17 NVRVD, 3 JNWZP => 8 VPVL
            53 STKFG, 6 MNCFX, 46 VJHF, 81 HVMC, 68 CXFTF, 25 GNMV => 1 FUEL
            22 VJHF, 37 MNCFX => 5 FWMGM
            139 ORE => 4 NVRVD
            144 ORE => 7 JNWZP
            5 MNCFX, 7 RFSQX, 2 FWMGM, 2 VPVL, 19 CXFTF => 3 HVMC
            5 VJHF, 7 MNCFX, 9 VPVL, 37 CXFTF => 6 GNMV
            145 ORE => 6 MNCFX
            1 NVRVD => 8 CXFTF
            1 VJHF, 6 MNCFX => 4 RFSQX
            176 ORE => 6 VJHF
            """,
            "180697",
            """
            171 ORE => 8 CNZTR
            7 ZLQW, 3 BMBT, 9 XCVML, 26 XMNCP, 1 WPTQ, 2 MZWV, 1 RJRHP => 4 PLWSL
            114 ORE => 4 BHXH
            14 VRPVC => 6 BMBT
            6 BHXH, 18 KTJDG, 12 WPTQ, 7 PLWSL, 31 FHTLT, 37 ZDVW => 1 FUEL
            6 WPTQ, 2 BMBT, 8 ZLQW, 18 KTJDG, 1 XMNCP, 6 MZWV, 1 RJRHP => 6 FHTLT
            15 XDBXC, 2 LTCX, 1 VRPVC => 6 ZLQW
            13 WPTQ, 10 LTCX, 3 RJRHP, 14 XMNCP, 2 MZWV, 1 ZLQW => 1 ZDVW
            5 BMBT => 4 WPTQ
            189 ORE => 9 KTJDG
            1 MZWV, 17 XDBXC, 3 XCVML => 2 XMNCP
            12 VRPVC, 27 CNZTR => 2 XDBXC
            15 KTJDG, 12 BHXH => 5 XCVML
            3 BHXH, 2 VRPVC => 7 MZWV
            121 ORE => 7 VRPVC
            7 XCVML => 6 RJRHP
            5 BHXH, 4 VRPVC => 5 LTCX
            """,
            "2210736"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            157 ORE => 5 NZVS
            165 ORE => 6 DCFZ
            44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL
            12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ
            179 ORE => 7 PSHF
            177 ORE => 5 HKGWZ
            7 DCFZ, 7 PSHF => 2 XJWVT
            165 ORE => 2 GPVTF
            3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT
            """,
            "82892753",
            """
            2 VPVL, 7 FWMGM, 2 CXFTF, 11 MNCFX => 1 STKFG
            17 NVRVD, 3 JNWZP => 8 VPVL
            53 STKFG, 6 MNCFX, 46 VJHF, 81 HVMC, 68 CXFTF, 25 GNMV => 1 FUEL
            22 VJHF, 37 MNCFX => 5 FWMGM
            139 ORE => 4 NVRVD
            144 ORE => 7 JNWZP
            5 MNCFX, 7 RFSQX, 2 FWMGM, 2 VPVL, 19 CXFTF => 3 HVMC
            5 VJHF, 7 MNCFX, 9 VPVL, 37 CXFTF => 6 GNMV
            145 ORE => 6 MNCFX
            1 NVRVD => 8 CXFTF
            1 VJHF, 6 MNCFX => 4 RFSQX
            176 ORE => 6 VJHF
            """,
            "5586022",
            """
            171 ORE => 8 CNZTR
            7 ZLQW, 3 BMBT, 9 XCVML, 26 XMNCP, 1 WPTQ, 2 MZWV, 1 RJRHP => 4 PLWSL
            114 ORE => 4 BHXH
            14 VRPVC => 6 BMBT
            6 BHXH, 18 KTJDG, 12 WPTQ, 7 PLWSL, 31 FHTLT, 37 ZDVW => 1 FUEL
            6 WPTQ, 2 BMBT, 8 ZLQW, 18 KTJDG, 1 XMNCP, 6 MZWV, 1 RJRHP => 6 FHTLT
            15 XDBXC, 2 LTCX, 1 VRPVC => 6 ZLQW
            13 WPTQ, 10 LTCX, 3 RJRHP, 14 XMNCP, 2 MZWV, 1 ZLQW => 1 ZDVW
            5 BMBT => 4 WPTQ
            189 ORE => 9 KTJDG
            1 MZWV, 17 XDBXC, 3 XCVML => 2 XMNCP
            12 VRPVC, 27 CNZTR => 2 XDBXC
            15 KTJDG, 12 BHXH => 5 XCVML
            3 BHXH, 2 VRPVC => 7 MZWV
            121 ORE => 7 VRPVC
            7 XCVML => 6 RJRHP
            5 BHXH, 4 VRPVC => 5 LTCX
            """,
            "460664"
        );
    }

    private static final Pattern pattern = Pattern.compile("(\\d+)\\s+(\\w+)");

    @Override
    public String part1Solution(String input, boolean isTest) {
        var formulas = parseFormulas(input);

        return calculateOre(formulas, Map.of("FUEL", 1L)) + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var formulas = parseFormulas(input);
        var oreForOneFuel = calculateOre(formulas, Map.of("FUEL", 1L));
        var maxOre = 1_000_000_000_000L;
        var count = maxOre / oreForOneFuel;
        var window = 100_000L;

        while (true) {
            count += window;
            var result = calculateOre(formulas, Map.of("FUEL", count));

            if (result > maxOre) {
                if (window == 1) {
                    return (count - 1) + "";
                } else {
                    count -= window;
                    window /= 10;
                }
            }
        }
    }

    private Map<String, Formula> parseFormulas(String input) {
        var formulas = new HashMap<String, Formula>();

        for (var line: input.trim().split("\n")) {
            var pairs = new ArrayList<Pair<Integer, String>>();
            var matcher = pattern.matcher(line);
            var map = new HashMap<String, Integer>();

            matcher.results().forEach(match -> {
                Integer count = Integer.parseInt(match.group(1));
                String name = match.group(2);
                pairs.add(Pair.of(count, name));
            });

            var target = pairs.removeLast();

            for (var pair: pairs) {
                map.put(pair.getRight(), pair.getLeft());
            }

            formulas.put(target.getRight(), new Formula(target.getLeft(), map));
        }

        return formulas;
    }

    private long calculateOre(Map<String, Formula> formulas, Map<String, Long> needed) {
        var extra = new HashMap<String, Long>();
        long ore = 0;

        while (!needed.isEmpty()) {
            var newNeeded = new HashMap<String, Long>();

            for (var entrySet: needed.entrySet()) {
                var name = entrySet.getKey();
                var count = entrySet.getValue();
                var availableExtra = extra.getOrDefault(name, 0L);
                extra.put(name, Math.max(0, availableExtra - count));
                count = Math.max(0, count - availableExtra);

                var formula = formulas.get(name);
                long multiplier = Math.ceilDiv(count, formula.count);

                for (var ingredientEntrySet: formula.ingredients.entrySet()) {
                    var ingredientName = ingredientEntrySet.getKey();
                    var ingredientCount = multiplier * ingredientEntrySet.getValue();

                    if (ingredientName.equals("ORE")) {
                        ore += ingredientCount;
                    } else {
                        newNeeded.put(ingredientName, newNeeded.getOrDefault(ingredientName, 0L) + ingredientCount);
                    }
                }

                if (multiplier * formula.count > count) {
                    var extraCount = multiplier * formula.count - count;
                    extra.put(name, extra.getOrDefault(name, 0L) +  extraCount);
                }
            }

            needed = newNeeded;
        }

        return ore;
    }

    private record Formula(Integer count, Map<String, Integer> ingredients) {}
}
