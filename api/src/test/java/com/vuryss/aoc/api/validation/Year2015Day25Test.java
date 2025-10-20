package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@QuarkusTest
class Year2015Day25Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 25;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of(
                "To continue, please consult the code grid in the manual.  Enter the code at row 4, column 2.",
                new Answer("32451966", "Merry Christmas!")
            ),
            Arguments.of(
                "To continue, please consult the code grid in the manual.  Enter the code at row 1, column 5.",
                new Answer("10071777", "Merry Christmas!")
            ),
            Arguments.of(
                "To continue, please consult the code grid in the manual.  Enter the code at row 6, column 6.",
                new Answer("27995004", "Merry Christmas!")
            ),
            Arguments.of(
                "To continue, please consult the code grid in the manual.  Enter the code at row 2981, column 3075.",
                new Answer("9132360", "Merry Christmas!")
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
            Arguments.of("Enter the code at row 4."),
            Arguments.of("Enter the code at column 2."),
            Arguments.of("Enter the code at row abc, column 2."),
            Arguments.of("Enter the code at row 4, column abc."),
            Arguments.of("a".repeat(501))
        );
    }
}

