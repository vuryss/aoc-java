package com.vuryss.aoc.api.validation.validators;

public class Year2015Day11Validator extends BaseValidator {
    public Year2015Day11Validator() {
        minLength(6);
        maxLength(10);
        pattern("^[a-z]{6,10}$");
    }
}

