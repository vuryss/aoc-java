package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class Year2015Day7Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 7;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of(
                """
                123 -> x
                456 -> y
                x AND y -> d
                x OR y -> e
                x LSHIFT 2 -> f
                y RSHIFT 2 -> g
                NOT x -> h
                NOT y -> i
                543 -> a
                887 -> b
                """,
                new Answer("543", "543")
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
            Arguments.of("123"), // Missing arrow
            Arguments.of("123 -> "), // Missing target
            Arguments.of("-> a"), // Missing source
            Arguments.of("123 => a"), // Wrong arrow
            Arguments.of("x PLUS y -> a"), // Invalid operation
            Arguments.of("x AND -> a"), // Missing operand
            Arguments.of("AND y -> a"), // Missing operand
            Arguments.of("x AND y ->"), // Missing target
            Arguments.of("x AND y -> abcd"), // Target too long (>3 chars)
            Arguments.of("NOT -> a"), // Missing operand
            Arguments.of("NOT x ->"), // Missing target
            Arguments.of("x LSHIFT -> a"), // Missing shift amount
            Arguments.of("x RSHIFT y z -> a"), // Too many operands
            Arguments.of("123 -> a\nabc"), // Invalid line in multi-line
            Arguments.of("123 -> a\n456 -> "), // Invalid line in multi-line
            Arguments.of("a".repeat(10001)) // Too long
        );
    }

    @Test
    public void testErrorHandling() {
        given()
            .contentType("text/plain")
            .body("""
            aa OR 12 -> bb
            """)
        .when()
            .post("/solve/2015/7")
        .then()
            .statusCode(400)
            .header("Content-Type", "application/problem+json")
            .body("status", equalTo(400))
            .body("title", equalTo("Bad Request"))
            .body("detail", equalTo("Cannot execute solution, please verify your input or contact the administrator."))
            .body("instance", equalTo("/solve/2015/7"));
    }
}

