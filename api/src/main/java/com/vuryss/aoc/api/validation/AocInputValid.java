package com.vuryss.aoc.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AocInputValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface AocInputValid {
    String message() default "Invalid input. Please check the documentation for the puzzle.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
