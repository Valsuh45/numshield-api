package com.numshield.numshield_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for Cameroon phone number validation.
 */
@Documented
@Constraint(validatedBy = CameroonPhoneConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCameroonPhone {
    String message() default "Invalid Cameroon phone number format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
