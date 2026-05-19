package com.numshield.numshield_api.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CameroonPhoneNumberNormalizerTest {

    @ParameterizedTest
    @CsvSource({
            // Local mobile numbers (start with 6)
            "690123456, +237690123456",
            "650123456, +237650123456",
            "670123456, +237670123456",
            
            // Local fixed-line numbers (start with 2)
            "222123456, +237222123456",
            "233123456, +237233123456",
            
            // Country-code format without '+'
            "237690123456, +237690123456",
            "237222123456, +237222123456",

            // Country-code format with '+' (already normalized)
            "+237690123456, +237690123456",
            "+237222123456, +237222123456",

            // Prefix with '00'
            "00237690123456, +237690123456",
            "00237222123456, +237222123456",

            // Numbers with various formatting characters (spaces, hyphens, dots, parentheses)
            "690 12 34 56, +237690123456",
            "690-12-34-56, +237690123456",
            "690.12.34.56, +237690123456",
            "(237) 690 123 456, +237690123456",
            "+237-690-123-456, +237690123456",
            "  +237 222 123 456  , +237222123456"
    })
    void shouldNormalizeValidCameroonPhoneNumbers(String input, String expected) {
        String result = CameroonPhoneNumberNormalizer.normalize(input);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n", "\r"})
    void shouldThrowExceptionForNullEmptyOrWhitespaceInputs(String input) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CameroonPhoneNumberNormalizer.normalize(input)
        );
        String expectedMessage = (input == null) ? "Phone number cannot be null" : "Phone number cannot be empty or blank";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "- - -",
            "()",
            "  . . .  "
    })
    void shouldThrowExceptionForFormattingOnlyInputs(String input) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CameroonPhoneNumberNormalizer.normalize(input)
        );
        assertEquals("Phone number cannot be empty or blank after removing formatting characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "69012345a",           // trailing letter
            "+23769012345b",       // letter inside country code format
            "690-123-45@",         // special symbol
            "+237 690 123 45+",    // extra '+' at the end
            "690+123456",          // '+' in the middle
            "+237 690 123a456"     // character in the middle
    })
    void shouldRejectInputsWithInvalidCharacters(String input) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CameroonPhoneNumberNormalizer.normalize(input)
        );
        assertEquals("Phone number contains invalid characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "590123456",           // Invalid local prefix (must start with 2 or 6)
            "390123456",           // Invalid local prefix (must start with 2 or 6)
            "237590123456",         // Invalid country code national prefix
            "+237190123456",        // Invalid country code national prefix
            "00237790123456",       // Invalid country code national prefix
            "+590123456"            // Starts with +, but only has 9 digits total and starts with 5
    })
    void shouldRejectInputsWithInvalidCameroonPrefix(String input) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CameroonPhoneNumberNormalizer.normalize(input)
        );
        // Message will either indicate invalid Cameroon prefix or invalid country code
        String message = exception.getMessage();
        org.junit.jupiter.api.Assertions.assertTrue(
                message.contains("Invalid Cameroon phone number prefix") || 
                message.contains("Invalid country code") ||
                message.contains("Local phone number format cannot start with '+'") ||
                message.contains("Phone number has an invalid length or format")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "+234690123456",       // Nigeria (+234)
            "238690123456",        // Cape Verde (+238)
            "00238690123456"       // Cape Verde (+238)
    })
    void shouldRejectInputsWithWrongCountryCode(String input) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CameroonPhoneNumberNormalizer.normalize(input)
        );
        String message = exception.getMessage();
        org.junit.jupiter.api.Assertions.assertTrue(
                message.contains("Invalid country code") || 
                message.contains("Phone number has an invalid length or format")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "69012345",            // Local too short (8 digits)
            "6901234567",          // Local too long (10 digits)
            "+23769012345",        // Country code too short (11 digits)
            "+2376901234567",      // Country code too long (13 digits)
            "23769012345",         // Country code no plus too short (11 digits)
            "2376901234567"        // Country code no plus too long (13 digits)
    })
    void shouldRejectInputsWithWrongLength(String input) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CameroonPhoneNumberNormalizer.normalize(input)
        );
        String message = exception.getMessage();
        org.junit.jupiter.api.Assertions.assertTrue(
                message.contains("Phone number is too short") || 
                message.contains("Phone number has an invalid length")
        );
    }

    @Test
    void shouldRejectLocalFormatWithLeadingPlus() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CameroonPhoneNumberNormalizer.normalize("+690123456")
        );
        assertEquals("Local phone number format cannot start with '+'", exception.getMessage());
    }
}
