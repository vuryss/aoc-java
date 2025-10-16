package com.vuryss.aoc.api.validation.validators;

import com.vuryss.aoc.api.validation.ValidationException;

public class Year2015Day2Validator extends BaseValidator {
    public Year2015Day2Validator() {
        minLength(5);
        maxLength(10_000);
        pattern("^[\\d\\nx]+$");
    }

    @Override
    protected void manualValidation(String input) throws ValidationException {
        super.manualValidation(input);

        var regex = "^\\d+x\\d+x\\d+$";

        for (var line: input.trim().split("\n")) {
            if (!line.matches(regex)) {
                throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
            }
        }
    }
}

