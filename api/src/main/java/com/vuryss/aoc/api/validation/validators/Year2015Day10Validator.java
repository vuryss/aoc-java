package com.vuryss.aoc.api.validation.validators;

public class Year2015Day10Validator extends BaseValidator {
    public Year2015Day10Validator() {
        minLength(1);
        maxLength(100);
        pattern("^[1-9]+$");
    }
}

