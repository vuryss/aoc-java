package com.vuryss.aoc.api.validation.validators;

public class Year2015Day5Validator extends BaseValidator {
    public Year2015Day5Validator() {
        minLength(1);
        maxLength(20_000);
        pattern("^[a-z\\n]+$");
    }
}

