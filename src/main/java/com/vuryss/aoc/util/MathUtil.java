package com.vuryss.aoc.util;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.util.ArithmeticUtils;

import java.util.Collection;
import java.util.List;

public class MathUtil {
    public static long lcm(List<Long> numbers) {
        long result = numbers.getFirst();

        for (int i = 1; i < numbers.size(); i++) {
            result = ArithmeticUtils.lcm(result, numbers.get(i));
        }

        return result;
    }

    public static long lcm(Collection<Long> numbers) {
        return lcm(List.copyOf(numbers));
    }

    public static Pair<Double, Double> quadratic(double a, double b, double c) {
        var discriminant = b * b - 4 * a * c;
        var sqrt = Math.sqrt(Math.abs(discriminant));
        var denominator = 2 * a;

        return Pair.of((-b + sqrt) / denominator, (-b - sqrt) / denominator);
    }
}
