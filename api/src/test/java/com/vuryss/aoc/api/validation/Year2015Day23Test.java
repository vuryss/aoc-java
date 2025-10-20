package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class Year2015Day23Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 23;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of(
                """
                inc a
                jio a, +2
                tpl a
                inc a
                """,
                new Answer("0", "0")
            ),
            Arguments.of(
                """
                inc b
                jio b, +2
                tpl b
                inc b
                """,
                new Answer("2", "2")
            ),
            Arguments.of(
                """
                jio a, +19
                inc a
                tpl a
                inc a
                tpl a
                inc a
                tpl a
                tpl a
                inc a
                inc a
                tpl a
                tpl a
                inc a
                inc a
                tpl a
                inc a
                inc a
                tpl a
                jmp +23
                tpl a
                tpl a
                inc a
                inc a
                tpl a
                inc a
                inc a
                tpl a
                inc a
                tpl a
                inc a
                tpl a
                inc a
                tpl a
                inc a
                inc a
                tpl a
                inc a
                inc a
                tpl a
                tpl a
                inc a
                jio a, +8
                inc b
                jie a, +4
                tpl a
                inc a
                jmp +2
                hlf a
                jmp -7
                """,
                new Answer("184", "231")
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
            Arguments.of("inc c"),
            Arguments.of("inc a\njio a +2"),
            Arguments.of("inc a\njio a, 2"),
            Arguments.of("inc a\nJIO a, +2"),
            Arguments.of("inc a\njio a, +2.5"),
            Arguments.of("a".repeat(5001))
        );
    }

    @Test
    public void testCodeTimeoutHandling() {
        var body = """
            inc a
            jmp -1
            """.trim();

        given()
            .contentType("text/plain")
            .body(body)
        .when()
            .post("/solve/2015/23")
        .then()
            .statusCode(400)
            .header("Content-Type", "application/problem+json")
            .body("status", equalTo(400))
            .body("title", equalTo("Execution failed"))
            .body("instance", equalTo("/solve/2015/23"))
        ;
    }
}

