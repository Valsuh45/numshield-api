package com.numshield.numshield_api.util;

import com.numshield.numshield_api.exception.PhoneNumberValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CameroonPhoneNumberValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "+237690123456", // Mobile MTN/Orange
            "+237650123456", // Mobile Orange
            "+237670123456", // Mobile MTN
            "+237620123456"  // Mobile Nexttel
    })
    void shouldValidateValidNormalizedNumbers(String number) {
        assertTrue(CameroonPhoneNumberValidator.isValid(number));
        assertDoesNotThrow(() -> CameroonPhoneNumberValidator.validate(number));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "   "
    })
    void shouldRejectNullOrEmptyInputs(String input) {
        assertFalse(CameroonPhoneNumberValidator.isValid(input));
        PhoneNumberValidationException ex = assertThrows(
                PhoneNumberValidationException.class,
                () -> CameroonPhoneNumberValidator.validate(input)
        );
        assertEquals("Phone number cannot be null or empty", ex.getMessage());
    }

    @Test
    void shouldRejectNullInput() {
        assertFalse(CameroonPhoneNumberValidator.isValid(null));
        PhoneNumberValidationException ex = assertThrows(
                PhoneNumberValidationException.class,
                () -> CameroonPhoneNumberValidator.validate(null)
        );
        assertEquals("Phone number cannot be null or empty", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "+23769012345a",
            "+23769012345_",
            "+23769012-456",
            "+237 690123456"
    })
    void shouldRejectNonNumericCharacters(String input) {
        assertFalse(CameroonPhoneNumberValidator.isValid(input));
        PhoneNumberValidationException ex = assertThrows(
                PhoneNumberValidationException.class,
                () -> CameroonPhoneNumberValidator.validate(input)
        );
        assertEquals("Phone number must contain only numeric digits after the '+' prefix", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "+234690123456", // Nigeria
            "+337690123456", // France
            "+123769012345"  // Wrong code starting with +1
    })
    void shouldRejectUnsupportedCountryCodes(String input) {
        assertFalse(CameroonPhoneNumberValidator.isValid(input));
        PhoneNumberValidationException ex = assertThrows(
                PhoneNumberValidationException.class,
                () -> CameroonPhoneNumberValidator.validate(input)
        );
        assertTrue(ex.getMessage().contains("Unsupported country code") || ex.getMessage().contains("Only Cameroon"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "+23769012345",   // 12 chars (too short)
            "+2376901234567"  // 14 chars (too long)
    })
    void shouldRejectInvalidLengths(String input) {
        assertFalse(CameroonPhoneNumberValidator.isValid(input));
        PhoneNumberValidationException ex = assertThrows(
                PhoneNumberValidationException.class,
                () -> CameroonPhoneNumberValidator.validate(input)
        );
        assertEquals("Invalid phone number length: normalized number must be exactly 13 characters", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "+237290123456",  // Starts with 2 — not a mobile number
            "+237222123456",  // Fixed-line (not supported)
            "+237233123456",  // Fixed-line (not supported)
            "+237390123456",
            "+237490123456",
            "+237590123456",
            "+237790123456",
            "+237890123456",
            "+237990123456",
            "+237190123456",
            "+237090123456"
    })
    void shouldRejectInvalidNationalPrefixes(String input) {
        assertFalse(CameroonPhoneNumberValidator.isValid(input));
        PhoneNumberValidationException ex = assertThrows(
                PhoneNumberValidationException.class,
                () -> CameroonPhoneNumberValidator.validate(input)
        );
        assertEquals("Invalid Cameroon phone number prefix: national number must start with 6", ex.getMessage());
    }
}
