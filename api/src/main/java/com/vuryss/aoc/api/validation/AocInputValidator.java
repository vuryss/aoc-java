package com.vuryss.aoc.api.validation;

import com.vuryss.aoc.config.AoCConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

import java.util.Optional;

@ApplicationScoped
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class AocInputValidator implements ConstraintValidator<AocInputValid, Object[]> {
    @Override
    public boolean isValid(Object[] params, ConstraintValidatorContext context) {
        if (null == params || params.length != 3) {
            throw new IllegalArgumentException("Expected 3 parameters: year, day, input");
        }

        var year = (Integer) params[0];
        var day = (Integer) params[1];
        var value = (String) params[2];

        if (!AoCConfig.isValidDayForYear(year, day)) {
            String maxDayText;
            try {
                maxDayText = String.valueOf(AoCConfig.getMaxChallengesForYear(year));
            } catch (IllegalArgumentException e) {
                maxDayText = "not configured";
            }

            if (context != null) {
                context.disableDefaultConstraintViolation();
                context
                    .buildConstraintViolationWithTemplate(
                        String.format(
                            "Invalid day %d for year %d. Maximum day for year %d is %s",
                            day,
                            year,
                            year,
                            maxDayText
                        )
                    )
                    .addConstraintViolation();
            }
            return false;
        }

        var validator = getValidatorFor(year, day);

        if (validator.isEmpty()) {
            return true;
        }

        try {
            validator.get().validate(value);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    public Optional<InputValidator> getValidatorFor(int year, int day) {
        var className = String.format("com.vuryss.aoc.api.validation.validators.Year%sDay%sValidator", year, day);

        try {
            Class<?> dynamicClass = Class.forName(className);
            return Optional.of((InputValidator) dynamicClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
