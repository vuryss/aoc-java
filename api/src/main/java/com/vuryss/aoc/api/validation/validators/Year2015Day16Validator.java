package com.vuryss.aoc.api.validation.validators;

import com.vuryss.aoc.api.validation.ValidationException;

public class Year2015Day16Validator extends BaseValidator {
    public Year2015Day16Validator() {
        minLength(20);
        maxLength(50_000);
        pattern("^[A-Za-z\\s:,\\d\\n]+$");
    }

    @Override
    protected void manualValidation(String input) throws ValidationException {
        super.manualValidation(input);

        var regex = "^Sue \\d+: [a-z]+: \\d+, [a-z]+: \\d+, [a-z]+: \\d+$";

        for (var line: input.trim().split("\n")) {
            if (!line.matches(regex)) {
                throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
            }
        }
    }
}

