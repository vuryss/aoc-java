package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day22Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 22;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of(
                """
                Hit Points: 55
                Damage: 8
                """,
                new Answer("953", "1289")
            )
        );
    }

    static Stream<Arguments> invalidInputData() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of(" "),
            Arguments.of("\t"),
            Arguments.of("\n"),
            Arguments.of("abc"),
            Arguments.of("Hit Points: 55"),
            Arguments.of("Hit Points: 55\nDamage: 8\nArmor: 2"),
            Arguments.of("HitPoints: 55\nDamage: 8"),
            Arguments.of("Hit Points: abc\nDamage: 8"),
            Arguments.of("Damage: 8\nHit Points: 55"),
            Arguments.of("a".repeat(101))
        );
    }
}

