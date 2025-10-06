package com.vuryss.aoc.api.validation.validators;

public class Year2015Day6Validator extends BaseValidator {
    public Year2015Day6Validator() {
        minLength(10);
        maxLength(15_000);
        pattern("^(turn on|turn off|toggle) \\d{1,3},\\d{1,3} through \\d{1,3},\\d{1,3}(\\n(turn on|turn off|toggle) \\d{1,3},\\d{1,3} through \\d{1,3},\\d{1,3})*$");
    }
}

