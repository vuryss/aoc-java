package com.vuryss.aoc.api.validation.validators;

public class Year2015Day2Validator extends BaseValidator {
    public Year2015Day2Validator() {
        minLength(5);
        maxLength(10_000);
        pattern("^(\\d+x\\d+x\\d+\\n)*(\\d+x\\d+x\\d+)$");
    }
}

