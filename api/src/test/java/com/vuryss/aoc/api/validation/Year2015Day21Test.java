package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day21Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 21;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of(
                """
                Hit Points: 100
                Damage: 8
                Armor: 2
                """,
                new Answer("91", "158")
            ),
            Arguments.of(
                """
                Hit Points: 103
                Damage: 9
                Armor: 2
                """,
                new Answer("121", "201")
            ),
            Arguments.of(
                """
                Hit Points: 104
                Damage: 8
                Armor: 1
                """,
                new Answer("78", "148")
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
            Arguments.of("Hit Points: 100\nDamage: 8"),
            Arguments.of("Hit Points: 100\nDamage: 8\nArmor: 2\nExtra: 5"),
            Arguments.of("HitPoints: 100\nDamage: 8\nArmor: 2"),
            Arguments.of("Hit Points: abc\nDamage: 8\nArmor: 2"),
            Arguments.of("Damage: 8\nHit Points: 100\nArmor: 2"),
            Arguments.of("a".repeat(201))
        );
    }
}

