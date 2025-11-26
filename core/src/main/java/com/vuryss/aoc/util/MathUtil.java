package com.vuryss.aoc.util;

import org.apache.commons.math3.util.ArithmeticUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MathUtil {
    public record QuadraticRoots(double one, double two) {}

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

    public static QuadraticRoots quadratic(double a, double b, double c) {
        var discriminant = Math.fma(b, b, - 4 * a * c);

        if (discriminant < 0) {
            return null;
        }

        var sqrt = Math.sqrt(discriminant);
        var denominator = 2 * a;

        return new QuadraticRoots((-b + sqrt) / denominator, (-b - sqrt) / denominator);
    }

    public static List<Long> factors(long n) {
        if (n < 1) {
            return List.of();
        }

        if (n == 1) {
            return List.of(1L);
        }

        var factors = new ArrayList<Long>();
        var sqrt = Math.sqrt(n);

        for (var i = 1L; i <= sqrt; i++) {
            if (n % i == 0) {
                factors.add(i);
                factors.add(n / i);
            }
        }

        return factors;
    }

    public static long modInverseCoprime(long a, long b) {
        return BigInteger.valueOf(a).modInverse(BigInteger.valueOf(b)).longValue();
    }

    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if ((n & 1) == 0 || n % 3 == 0) return false;

        // Check 5, 7, 11, 13, 17, 19...
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    public static boolean isPrime(long n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if ((n & 1) == 0 || n % 3 == 0) return false;

        // Check 5, 7, 11, 13, 17, 19...
        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }
}
