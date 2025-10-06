package com.vuryss.aoc.api.validation.validators;

import com.vuryss.aoc.api.validation.ValidationException;

public class Year2015Day7Validator extends BaseValidator {
    public Year2015Day7Validator() {
        minLength(10);
        maxLength(10_000);
        pattern("^[a-zA-Z0-9\\n\\->\\s]+$");
    }

    @Override
    protected void manualValidation(String input) throws ValidationException {
        super.manualValidation(input);

        var regex = "^(NOT\\s[a-z]{1,3}\\s->\\s[a-z]{1,3}|[a-z0-9]{1,3}\\s(?:AND|OR|LSHIFT|RSHIFT)\\s[a-z0-9]{1,3}\\s->\\s[a-z]{1,3}|(?:\\d+|[a-z]{1,3})\\s->\\s[a-z]{1,3})$";

        for (var line: input.trim().split("\n")) {
            if (!line.matches(regex)) {
                throw new ValidationException("Invalid input. Please check the documentation for the puzzle.");
            }
        }
    }
}

