package com.vuryss.aoc.solutions.event2019;

import com.vuryss.aoc.solutions.SolutionInterface;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class Day22 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var cards = IntStream.range(0, 10007).toArray();
        var operations = parseOperations(input);
        var index = BigInteger.valueOf(2019);
        var equation = calculateEquationCoefficients(operations, cards.length);

        // position = a * card + b  (mod deckSize)
        return index.multiply(equation.a).add(equation.b).mod(BigInteger.valueOf(cards.length)).toString();
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var operations = parseOperations(input);
        var deckSize = BigInteger.valueOf(119315717514047L);
        var oneShuffle = calculateEquationCoefficients(operations, deckSize.longValue());
        var iterations = 101741582076661L;
        var index = BigInteger.valueOf(2020);
        var powers = new HashSet<Long>();

        // Extract the powers of 2 contained in the iterations
        while (iterations > 0) {
            var i = 1L;
            var power = 0L;
            while (i <= iterations) {
                i *= 2;
                power++;
            }
            iterations -= i/2;
            powers.add(power-1);
        }

        // position = a * card + b  (mod deckSize)
        var extrapolated = new Equation(BigInteger.ONE, BigInteger.ZERO);

        for (var power: powers) {
            var exponentiatedToPower = exponentiate(oneShuffle, deckSize, power); // This finds f^(2^power)
            extrapolated = extrapolated.compose(exponentiatedToPower, deckSize); // Composes the selected powers: f(g(x))
        }

        // Inversed of: position = a * card + b  (mod deckSize)
        // | position - b ≡ a * card  (mod M)
        // | card ≡ (position - b) * a⁻¹  (mod M)
        return index
            .subtract(extrapolated.b)
            .mod(deckSize)
            .multiply(extrapolated.a.modInverse(deckSize))
            .mod(deckSize)
            .toString();
    }

    private Equation exponentiate(Equation equation, BigInteger deckSize, long power) {
        for (var i = 0; i < power; i++) {
            equation = equation.compose(equation, deckSize);
        }

        return equation;
    }

    /**
     * All operations converted to a * x + b:
     *      deal with incr ->  a * x + 0
     *      cut            ->  1 * x - b
     *      reverse        -> -1 * x - 1
     *
     * Then if we have 2 operations:
     *
     * f(x) = a * x + b
     * g(x) = c * x + d
     * f(g(x)) = a * c * x + a * d + b
     */
    private Equation calculateEquationCoefficients(List<Operation> operations, long deckSize) {
        // 1 * x + 0 - base case
        long a = 1, b = 0;

        // Stored: f(x) = a * x + b
        // Incoming operation: g(x) = c * x + d
        // Result: c*a*x + c*b + d
        for (Operation operation: operations) {
            switch (operation.type) {
                // c = operation.value
                // d = 0
                case DEAL_INCR:
                    // a = c*a*x
                    a *= operation.value;
                    // b = c*b + d
                    b *= operation.value;
                    break;

                // c = 1
                // d = -operation.value
                case CUT:
                    // a = c*a*x, as c is 1 :: a does not change
                    // b = c*b + d
                    b -= operation.value;
                    break;

                // c = -1
                // d = -1
                case REVERSE:
                    // a = c*a*x
                    a *= -1;

                    // b = c*b + d
                    b = -b - 1;
                    break;
            }

            a = Math.floorMod(a, deckSize);
            b = Math.floorMod(b, deckSize);
        }

        return new Equation(BigInteger.valueOf(a), BigInteger.valueOf(b));
    }

    private List<Operation> parseOperations(String input) {
        var operations = new ArrayList<Operation>();

        for (var line: input.trim().split("\n")) {
            var parts = line.split(" ");
            if (line.startsWith("deal with")) operations.add(new Operation(Type.DEAL_INCR, Integer.parseInt(parts[3])));
            else if (line.startsWith("cut")) operations.add(new Operation(Type.CUT, Integer.parseInt(parts[1])));
            else operations.add(new Operation(Type.REVERSE, 0));
        }

        return operations;
    }

    private enum Type { DEAL_INCR, CUT, REVERSE }

    private record Operation(Type type, int value) {}

    private record Equation(BigInteger a, BigInteger b) {
        /**
         * Returns this(other(x)).
         *
         * this(x) = a*x + b
         * other(x) = c*x + d
         *
         * this(other(x)) = a*c*x + a*d + b
         */
        public Equation compose(Equation other, BigInteger deckSize) {
            return new Equation(
                a.multiply(other.a).mod(deckSize),
                a.multiply(other.b).add(b).mod(deckSize)
            );
        }
    }
}
