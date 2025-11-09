package com.vuryss.aoc.util;

public class Algorithm {
    public static long fib(long n) {
        return n <= 1 ? n : fib(n - 1) + fib(n - 2);
    }

    public static long chineseRemainderTheorem(long[] remainders, long[] moduli) {
        long product = 1;

        for (long modulus : moduli) {
            product *= modulus;
        }

        long result = 0;

        for (int i = 0; i < remainders.length; i++) {
            long partialProduct = product / moduli[i];
            long inverse = MathUtil.modInverseCoprime(partialProduct, moduli[i]);
            result += remainders[i] * partialProduct * inverse;
        }

        return result % product;
    }
}
