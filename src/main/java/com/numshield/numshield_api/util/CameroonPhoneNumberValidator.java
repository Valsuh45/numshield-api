package com.numshield.numshield_api.util;

import com.numshield.numshield_api.exception.PhoneNumberValidationException;

/**
 * Utility class for validating normalized Cameroon phone numbers.
 * Valid normalized format is "+237XXXXXXXXX" (where XXXXXXXXX starts with 6 — mobile only).
 *
 * <p>The only accepted country code is +237 (Cameroon).
 * Only mobile numbers (national subscriber number starting with 6) are supported.</p>
 */
public final class CameroonPhoneNumberValidator {

    private CameroonPhoneNumberValidator() {
        // Prevent instantiation
    }

    /**
     * Validates a normalized Cameroon phone number against Cameroon numbering standards.
     *
     * @param normalizedNumber the normalized phone number to validate (expected format +237XXXXXXXXX)
     * @throws PhoneNumberValidationException if the phone number does not conform to Cameroon numbering standards
     */
    public static void validate(String normalizedNumber) {
        if (normalizedNumber == null || normalizedNumber.trim().isEmpty()) {
            throw new PhoneNumberValidationException("Phone number cannot be null or empty");
        }

        // Check for non-numeric characters (except the leading +)
        if (!normalizedNumber.matches("^\\+?[\\d]+$")) {
            throw new PhoneNumberValidationException("Phone number must contain only numeric digits after the '+' prefix");
        }

        // Check unsupported country codes (must start with +237)
        if (!normalizedNumber.startsWith("+237")) {
            if (normalizedNumber.startsWith("+")) {
                String potentialCountryCode = normalizedNumber.length() >= 4 ? normalizedNumber.substring(1, 4) : normalizedNumber.substring(1);
                throw new PhoneNumberValidationException("Unsupported country code: +" + potentialCountryCode + ". Only Cameroon (+237) is supported.");
            } else {
                throw new PhoneNumberValidationException("Phone number must start with country code +237");
            }
        }

        // Check length
        // +237 (4 chars) + 9 digits = 13 chars
        if (normalizedNumber.length() != 13) {
            throw new PhoneNumberValidationException("Invalid phone number length: normalized number must be exactly 13 characters");
        }

        // Check Cameroon specific subscriber numbering plan
        // The 9-digit national number starts at index 4 (after "+237")
        // Only mobile numbers (starting with 6) are currently supported
        char firstNationalDigit = normalizedNumber.charAt(4);
        if (firstNationalDigit != '6') {
            throw new PhoneNumberValidationException("Invalid Cameroon phone number prefix: national number must start with 6");
        }
    }

    /**
     * Checks if a normalized phone number is valid.
     *
     * @param normalizedNumber the normalized phone number
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String normalizedNumber) {
        try {
            validate(normalizedNumber);
            return true;
        } catch (PhoneNumberValidationException e) {
            return false;
        }
    }
}
