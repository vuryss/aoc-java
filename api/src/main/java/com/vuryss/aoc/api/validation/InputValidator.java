package com.vuryss.aoc.api.validation;

public interface InputValidator {
    void validate(String input) throws ValidationException;
}

