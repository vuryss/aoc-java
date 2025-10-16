package com.vuryss.aoc.api.validation.validators;

import com.vuryss.aoc.api.validation.ValidationException;

public class Year2015Day13Validator extends BaseValidator {
    public Year2015Day13Validator() {
        minLength(20);
        maxLength(10_000);
        pattern("^[A-Za-z\\s.\\d\\n]+$");
    }

    @Override
    protected void manualValidation(String input) throws ValidationException {
        super.manualValidation(input);

        var regex = "^[A-Za-z]+ would (gain|lose) \\d+ happiness units by sitting next to [A-Za-z]+\\.$";

        for (var line: input.trim().split("\n")) {
            if (!line.matches(regex)) {
                throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
            }
        }
    }
}

