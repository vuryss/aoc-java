package com.vuryss.aoc.api.validation.validators;

import com.vuryss.aoc.api.validation.ValidationException;

public class Year2015Day25Validator extends BaseValidator {
    public Year2015Day25Validator() {
        minLength(20);
        maxLength(150);
        pattern("^[A-Za-z\\s,.'\\d\\n]+$");
    }

    @Override
    protected void manualValidation(String input) throws ValidationException {
        super.manualValidation(input);

        var regex = "^.*row \\d+.*column \\d+.*$";

        if (!input.trim().matches(regex)) {
            throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
        }
    }
}
