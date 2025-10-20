package com.vuryss.aoc.api.validation.validators;

public class Year2015Day24Validator extends BaseValidator {
    public Year2015Day24Validator() {
        minLength(1);
        maxLength(1_000);
        pattern("^\\d+(\\n\\d+)*\\n?$");
    }
}

