package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.Utils;
import com.vuryss.aoc.solutions.DayInterface;

import java.util.Map;

public class Day25 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            1=-0-2
            12111
            2=0=
            21
            2=01
            111
            20012
            112
            1=-1=
            1-12
            12
            1=
            122
            """,
            "2=-1=0"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of("", "");
    }

    @Override
    public String part1Solution(String input) {
        var snafuNumbers = input.trim().split("\n");
        long decimalSum = 0;

        for (var snafuNumber: snafuNumbers) {
            decimalSum += snafuToDecimal(snafuNumber);
        }

        return decimalToSnafu(decimalSum);
    }

    String decimalToSnafu(long decimal) {
        String snafu = "";
        byte power = 0;

        while (maxPowerValue(2, power) < decimal) {
            power++;
        }

        while (power >= 0) {
            for (var multiplier = -2; multiplier <= 2; multiplier++) {
                if (decimal >= minPowerValue(multiplier, power) && decimal <= maxPowerValue(multiplier, power)) {
                    snafu += switch (multiplier) {
                        case -2 -> "=";
                        case -1 -> "-";
                        case 0 -> "0";
                        case 1 -> "1";
                        case 2 -> "2";
                        default -> throw new IllegalStateException("Unexpected value: " + multiplier);
                    };
                    decimal -= (long) multiplier * (long) Math.pow(5L, (long) power);
                    break;
                }
            }

            power--;
        }

        return snafu;
    }

    long snafuToDecimal(String snafu) {
        var chars = Utils.toCharacterQueue(snafu);
        long decimal = 0L;
        byte power = 0;

        while (!chars.isEmpty()) {
            var digit = chars.pollLast();

            if (digit == '-') {
                decimal += -1 * Math.pow(5, power);
            } else if (digit == '=') {
                decimal += -2 * Math.pow(5, power);
            } else {
                decimal += Character.getNumericValue(digit) * Math.pow(5, power);
            }

            power++;
        }

        return decimal;
    }

    long minPowerValue(int multiplier, int power) {
        long minPowerValue = (long) multiplier * (long) Math.pow(5L, (long) power);

        for (int lowerPower = power - 1; lowerPower >= 0; lowerPower--) {
            minPowerValue -= 2L * (long) Math.pow(5L, (long) lowerPower);
        }

        return minPowerValue;
    }

    long maxPowerValue(int multiplier, int power) {
        long maxPowerValue = (long) multiplier * (long) Math.pow(5L, (long) power);

        for (int lowerPower = power - 1; lowerPower >= 0; lowerPower--) {
            maxPowerValue += 2L * (long) Math.pow(5L, (long) lowerPower);
        }

        return maxPowerValue;
    }

    @Override
    public String part2Solution(String input) {
        return "";
    }
}
