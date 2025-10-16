package com.vuryss.aoc.api.validation.validators;

import com.vuryss.aoc.api.validation.ValidationException;

public class Year2015Day9Validator extends BaseValidator {
    public Year2015Day9Validator() {
        minLength(10);
        maxLength(10_000);
        pattern("^[A-Za-z\\s=\\d\\n]+$");
    }

    @Override
    protected void manualValidation(String input) throws ValidationException {
        super.manualValidation(input);

        var regex = "^[A-Za-z]+ to [A-Za-z]+ = \\d+$";

        for (var line: input.trim().split("\n")) {
            if (!line.matches(regex)) {
                throw new ValidationException("Invalid input. Please check the documentation for the puzzle!!.");
            }
        }
    }
}

