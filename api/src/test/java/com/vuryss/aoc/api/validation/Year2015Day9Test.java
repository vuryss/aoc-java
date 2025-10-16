package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class Year2015Day9Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 9;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of(
                """
                London to Dublin = 464
                London to Belfast = 518
                Dublin to Belfast = 141
                """,
                new Answer("605", "982")
            ),
            Arguments.of(
                """
                AlphaCentauri to Snowdin = 66
                AlphaCentauri to Tambi = 28
                AlphaCentauri to Faerun = 60
                AlphaCentauri to Norrath = 34
                AlphaCentauri to Straylight = 34
                AlphaCentauri to Tristram = 3
                AlphaCentauri to Arbre = 108
                Snowdin to Tambi = 22
                Snowdin to Faerun = 12
                Snowdin to Norrath = 91
                Snowdin to Straylight = 121
                Snowdin to Tristram = 111
                Snowdin to Arbre = 71
                Tambi to Faerun = 39
                Tambi to Norrath = 113
                Tambi to Straylight = 130
                Tambi to Tristram = 35
                Tambi to Arbre = 40
                Faerun to Norrath = 63
                Faerun to Straylight = 21
                Faerun to Tristram = 57
                Faerun to Arbre = 83
                Norrath to Straylight = 9
                Norrath to Tristram = 50
                Norrath to Arbre = 60
                Straylight to Tristram = 27
                Straylight to Arbre = 81
                Tristram to Arbre = 90
                """,
                new Answer("141", "736")
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
            Arguments.of("London Dublin = 464"), // Missing "to"
            Arguments.of("London to Dublin 464"), // Missing "="
            Arguments.of("London to Dublin ="), // Missing distance
            Arguments.of("London to Dublin = abc"), // Non-numeric distance
            Arguments.of("London to Dublin = -5"), // Negative distance
            Arguments.of("London  to  Dublin = 464"), // Extra spaces
            Arguments.of("a".repeat(10001)) // Too long
        );
    }

    @Test
    public void testCodeExceptionHandling() {
        given()
            .contentType("text/plain")
            .body("""
            London to Dublin = 798798789797987987979798979797987987979797979879879
            """)
        .when()
            .post("/solve/2015/9")
        .then()
            .statusCode(400)
            .header("Content-Type", "application/problem+json")
            .body("status", equalTo(400))
            .body("title", equalTo("Bad Request"))
            .body("detail", equalTo("Cannot execute solution, please verify your input or contact the administrator."))
            .body("instance", equalTo("/solve/2015/9"))
        ;
    }

    @Test
    public void testOutOfMemoryHandling() {
        given()
            .contentType("text/plain")
            .body("""
            AlphaCentauri to Snowdin = 66
            AlphaCentauri to Tambi = 28
            AlphaCentauri to Faerun = 60
            AlphaCentauri to Norrath = 34
            AlphaCentauri to Straylight = 34
            AlphaCentauri to Tristram = 3
            AlphaCentauri to Arbre = 108
            AlphaCentauri to Valhalla = 92
            AlphaCentauri to Umbriel = 47
            AlphaCentauri to Xanadu = 18
            AlphaCentauri to Zanzibar = 75
            AlphaCentauri to Olympus = 53
            AlphaCentauri to Rivendell = 89
            AlphaCentauri to Mordor = 12
            AlphaCentauri to Atlantis = 101
            Snowdin to Tambi = 22
            Snowdin to Faerun = 12
            Snowdin to Norrath = 91
            Snowdin to Straylight = 121
            Snowdin to Tristram = 111
            Snowdin to Arbre = 71
            Snowdin to Valhalla = 8
            Snowdin to Umbriel = 103
            Snowdin to Xanadu = 52
            Snowdin to Zanzibar = 29
            Snowdin to Olympus = 97
            Snowdin to Rivendell = 45
            Snowdin to Mordor = 78
            Snowdin to Atlantis = 14
            Tambi to Faerun = 39
            Tambi to Norrath = 113
            Tambi to Straylight = 130
            Tambi to Tristram = 35
            Tambi to Arbre = 40
            Tambi to Valhalla = 19
            Tambi to Umbriel = 86
            Tambi to Xanadu = 44
            Tambi to Zanzibar = 68
            Tambi to Olympus = 25
            Tambi to Rivendell = 107
            Tambi to Mordor = 31
            Tambi to Atlantis = 23
            Faerun to Norrath = 63
            Faerun to Straylight = 21
            Faerun to Tristram = 57
            Faerun to Arbre = 83
            Faerun to Valhalla = 5
            Faerun to Umbriel = 98
            Faerun to Xanadu = 72
            Faerun to Zanzibar = 31
            Faerun to Olympus = 84
            Faerun to Rivendell = 56
            Faerun to Mordor = 70
            Faerun to Atlantis = 11
            Norrath to Straylight = 9
            Norrath to Tristram = 50
            Norrath to Arbre = 60
            Norrath to Valhalla = 88
            Norrath to Umbriel = 25
            Norrath to Xanadu = 41
            Norrath to Zanzibar = 117
            Norrath to Olympus = 36
            Norrath to Rivendell = 94
            Norrath to Mordor = 43
            Norrath to Atlantis = 99
            Straylight to Tristram = 27
            Straylight to Arbre = 81
            Straylight to Valhalla = 16
            Straylight to Umbriel = 7
            Straylight to Xanadu = 38
            Straylight to Zanzibar = 124
            Straylight to Olympus = 112
            Straylight to Rivendell = 64
            Straylight to Mordor = 37
            Straylight to Atlantis = 128
            Tristram to Arbre = 90
            Tristram to Valhalla = 106
            Tristram to Umbriel = 54
            Tristram to Xanadu = 20
            Tristram to Zanzibar = 77
            Tristram to Olympus = 49
            Tristram to Rivendell = 85
            Tristram to Mordor = 15
            Tristram to Atlantis = 119
            Arbre to Valhalla = 69
            Arbre to Umbriel = 65
            Arbre to Xanadu = 115
            Arbre to Zanzibar = 48
            Arbre to Olympus = 93
            Arbre to Rivendell = 28
            Arbre to Mordor = 105
            Arbre to Atlantis = 76
            Valhalla to Umbriel = 110
            Valhalla to Xanadu = 85
            Valhalla to Zanzibar = 33
            Valhalla to Olympus = 100
            Valhalla to Rivendell = 62
            Valhalla to Mordor = 96
            Valhalla to Atlantis = 4
            Umbriel to Xanadu = 58
            Umbriel to Zanzibar = 95
            Umbriel to Olympus = 26
            Umbriel to Rivendell = 118
            Umbriel to Mordor = 51
            Umbriel to Atlantis = 109
            Xanadu to Zanzibar = 62
            Xanadu to Olympus = 46
            Xanadu to Rivendell = 91
            Xanadu to Mordor = 17
            Xanadu to Atlantis = 87
            Zanzibar to Olympus = 73
            Zanzibar to Rivendell = 39
            Zanzibar to Mordor = 80
            Zanzibar to Atlantis = 30
            Olympus to Rivendell = 104
            Olympus to Mordor = 55
            Olympus to Atlantis = 102
            Rivendell to Mordor = 92
            Rivendell to Atlantis = 67
            Mordor to Atlantis = 114
            """)
        .when()
            .post("/solve/2015/9")
        .then()
            .statusCode(400)
            .header("Content-Type", "application/problem+json")
            .body("status", equalTo(400))
            .body("title", equalTo("Bad Request"))
            .body("detail", equalTo("Cannot execute solution, please verify your input or contact the administrator."))
            .body("instance", equalTo("/solve/2015/9"))
        ;
    }
}

