package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.api.dto.Answer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class Year2015Day19Test extends BaseValidatorTest {
    @Override
    protected int getYear() {
        return 2015;
    }

    @Override
    protected int getDay() {
        return 19;
    }

    static Stream<Arguments> validInputData() {
        return Stream.of(
            Arguments.of(
                """
                Al => ThF
                Al => ThRnFAr
                B => BCa
                B => TiB
                B => TiRnFAr
                Ca => CaCa
                Ca => PB
                Ca => PRnFAr
                Ca => SiRnFYFAr
                Ca => SiRnMgAr
                Ca => SiTh
                F => CaF
                F => PMg
                F => SiAl
                H => CRnAlAr
                H => CRnFYFYFAr
                H => CRnFYMgAr
                H => CRnMgYFAr
                H => HCa
                H => NRnFYFAr
                H => NRnMgAr
                H => NTh
                H => OB
                H => ORnFAr
                Mg => BF
                Mg => TiMg
                N => CRnFAr
                N => HSi
                O => CRnFYFAr
                O => CRnMgAr
                O => HP
                O => NRnFAr
                O => OTi
                P => CaP
                P => PTi
                P => SiRnFAr
                Si => CaSi
                Th => ThCa
                Ti => BP
                Ti => TiTi
                e => HF
                e => NAl
                e => OMg
                
                CRnSiRnCaPTiMgYCaPTiRnFArSiThFArCaSiThSiThPBCaCaSiRnSiRnTiTiMgArPBCaPMgYPTiRnFArFArCaSiRnBPMgArPRnCaPTiRnFArCaSiThCaCaFArPBCaCaPTiTiRnFArCaSiRnSiAlYSiThRnFArArCaSiRnBFArCaCaSiRnSiThCaCaCaFYCaPTiBCaSiThCaSiThPMgArSiRnCaPBFYCaCaFArCaCaCaCaSiThCaSiRnPRnFArPBSiThPRnFArSiRnMgArCaFYFArCaSiRnSiAlArTiTiTiTiTiTiTiRnPMgArPTiTiTiBSiRnSiAlArTiTiRnPMgArCaFYBPBPTiRnSiRnMgArSiThCaFArCaSiThFArPRnFArCaSiRnTiBSiThSiRnSiAlYCaFArPRnFArSiThCaFArCaCaSiThCaCaCaSiRnPRnCaFArFYPMgArCaPBCaPBSiRnFYPBCaFArCaSiAl
                """.trim(),
                new Answer("518", "200")
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
            Arguments.of("H => HO\nHOH"),
            Arguments.of("H => HO\n\nHOH\n\nExtra"),
            Arguments.of("H HO\n\nHOH"),
            Arguments.of("H => HO\n\n123"),
            Arguments.of("a".repeat(10001))
        );
    }

    @Test
    public void testCodeTimeoutHandling() {
        var body = """
            Al => ThF
            Al => ThRnFAr
            B => BCa
            B => TiB
            B => TiRnFAr
            Ca => CaCa
            Ca => PB
            Ca => PRnFAr
            Ca => SiRnFYFAr
            Ca => SiRnMgAr
            Ca => SiTh
            F => CaF
            F => PMg
            F => SiAl
            H => CRnAlAr
            H => CRnFYFYFAr
            H => CRnFYMgAr
            H => CRnMgYFAr
            H => HCa
            H => NRnFYFAr
            H => NRnMgAr
            H => NTh
            H => OB
            H => ORnFAr
            Mg => BF
            Mg => TiMg
            N => CRnFAr
            N => HSi
            O => CRnFYFAr
            O => CRnMgAr
            O => HP
            O => NRnFAr
            O => OTi
            P => CaP
            P => PTi
            P => SiRnFAr
            Si => CaSi
            Th => ThCa
            Ti => BP
            Ti => TiTi
            e => HF
            e => NAl
            e => OMg
            
            CRnCaSiRnBSiRnFArTiBPTiTiBFArPBCaSiThSiRnTiBPBPMgArCaSiRnTiMgArCaSiThCaSiRnFArRnSiRnFArTiTiBFArCaCaSiRnSiThCaCaSiRnMgArFYSiRnFYCaFArSiThCaSiThPBPTiMgArCaPRnSiAlArPBCaCaSiRnFYSiThCaRnFArArCaCaSiRnPBSiRnFArMgYCaCaCaCaSiThCaCaSiAlArCaCaSiRnPBSiAlArBCaCaCaCaSiThCaPBSiThPBPBCaSiRnFYFArSiThCaSiRnFArBCaCaSiRnFYFArSiThCaPBSiThCaSiRnPMgArRnFArPTiBCaPRnFArCaCaCaCaSiRnCaCaSiRnFYFArFArBCaSiThFArThSiThSiRnTiRnPMgArFArCaSiThCaPBCaSiRnBFArCaCaPRnCaCaPMgArSiRnFYFArCaSiThRnPBPMgAr
            """.trim();

        given()
            .contentType("text/plain")
            .body(body)
        .when()
            .post("/solve/2015/19")
        .then()
            .statusCode(400)
            .header("Content-Type", "application/problem+json")
            .body("status", equalTo(400))
            .body("title", equalTo("Execution failed"))
            .body("instance", equalTo("/solve/2015/19"))
        ;
    }
}

