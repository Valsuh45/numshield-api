package com.numshield.numshield_api.validation;

import com.numshield.numshield_api.exception.PhoneNumberValidationException;
import com.numshield.numshield_api.util.CameroonPhoneNumberNormalizer;
import com.numshield.numshield_api.util.CameroonPhoneNumberValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Constraint validator for Cameroon phone numbers.
 * Integrates normalization and validation.
 */
public class CameroonPhoneConstraintValidator implements ConstraintValidator<ValidCameroonPhone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            // Allow null/empty to pass validator (use @NotNull or @NotEmpty if required)
            return true;
        }

        try {
            // 1. Normalization occurs first
            String normalized = CameroonPhoneNumberNormalizer.normalize(value);

            // 2. Validation occurs after normalization
            CameroonPhoneNumberValidator.validate(normalized);
            return true;
        } catch (IllegalArgumentException | PhoneNumberValidationException e) {
            // Propagate the specific, descriptive validation error message
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                   .addConstraintViolation();
            return false;
        }
    }
}
