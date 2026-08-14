package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.MathUtil;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day13 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            939
            7,13,x,x,59,x,31,19
            """,
            "295"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            x
            7,13,x,x,59,x,31,19
            """,
            "1068781",
            """
            x
            17,x,13,19
            """,
            "3417",
            """
            x
            67,7,59,61
            """,
            "754018",
            """
            x
            67,x,7,59,61
            """,
            "779210",
            """
            x
            67,7,x,59,61
            """,
            "1261476",
            """
            x
            1789,37,47,1889
            """,
            "1202161486"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var busses = StringUtil.ints(input);
        var target = busses.removeFirst();
        var closest = 0;
        var minDistance = Integer.MAX_VALUE;

        for (var id : busses) {
            var distance = (((target / id) + 1) * id) - target;
            if (distance < minDistance) {
                minDistance = distance;
                closest = id;
            }
        }

        return (closest * minDistance) + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var lines = input.trim().split("\n");
        var elems = lines[1].split(",");
        var mods = new LinkedHashMap<Long, Long>();

        for (var i = 0; i < elems.length; i++) {
            var elem = elems[i];
            if (elem.equals("x")) continue;
            var num = Long.parseLong(elem);

            mods.put(num, Math.floorMod((num - i), num));
        }

        long result = 0;
        long lcm = 1;

        for (var es: mods.entrySet()) {
            long nextMod = es.getKey();
            long nextRem = es.getValue();

            while ((result - nextRem) % nextMod != 0) result += lcm;

            lcm = MathUtil.lcm(lcm, nextMod);
        }

        return result + "";
    }
}