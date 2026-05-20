package com.numshield.numshield_api.util;

/**
 * Utility class for normalizing Cameroon phone numbers.
 * The normalized format is always {@code +237XXXXXXXXX} (where XXXXXXXXX are 9 digits starting with 6).
 *
 * <p>Currently only mobile numbers (prefix 6) are supported.
 * The only accepted country code is +237 (Cameroon).</p>
 */
public final class CameroonPhoneNumberNormalizer {

    private CameroonPhoneNumberNormalizer() {
        // Prevent instantiation
    }

    /**
     * Normalizes a Cameroon phone number.
     *
     * @param phoneNumber the raw phone number to normalize
     * @return the normalized phone number in the format +237XXXXXXXXX
     * @throws IllegalArgumentException if the input is null, empty, blank, or does not represent a valid Cameroon phone number format
     */
    public static String normalize(String phoneNumber) {
        if (phoneNumber == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }

        String trimmed = phoneNumber.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty or blank");
        }

        // Verify that the phone number contains only valid characters:
        // Optional leading plus, digits, spaces, hyphens, dots, or parentheses
        if (!trimmed.matches("^\\+?[\\d\\s\\-\\(\\)\\.]+$")) {
            throw new IllegalArgumentException("Phone number contains invalid characters");
        }

        // Remove all formatting characters (spaces, hyphens, parentheses, dots)
        String cleaned = trimmed.replaceAll("[\\s\\-\\(\\)\\.]+", "");

        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty or blank after removing formatting characters");
        }

        boolean hasPlus = cleaned.startsWith("+");
        String digits = hasPlus ? cleaned.substring(1) : cleaned;

        // Ensure there are only digits
        if (!digits.matches("\\d+")) {
            throw new IllegalArgumentException("Phone number must contain only digits after formatting characters are removed");
        }

        int length = digits.length();

        // 1. Local format: exactly 9 digits, e.g. 690123456
        if (length == 9) {
            if (hasPlus) {
                throw new IllegalArgumentException("Local phone number format cannot start with '+'");
            }
            if (digits.charAt(0) != '6') {
                throw new IllegalArgumentException("Invalid Cameroon phone number prefix: must start with 6");
            }
            return "+237" + digits;
        }

        // 2. Country code format: exactly 12 digits, e.g. 237690123456 or +237690123456
        if (length == 12) {
            if (!digits.startsWith("237")) {
                throw new IllegalArgumentException("Invalid country code: " + digits.substring(0, 3) + " (only Cameroon (+237) is supported)");
            }
            if (digits.charAt(3) != '6') {
                throw new IllegalArgumentException("Invalid Cameroon phone number prefix: must start with 6");
            }
            return "+237" + digits.substring(3);
        }

        // 3. Country code format with 00 prefix: e.g. 00237690123456 (14 digits)
        if (length == 14 && digits.startsWith("00237")) {
            if (hasPlus) {
                throw new IllegalArgumentException("Phone number starting with '00' cannot also start with '+'");
            }
            if (digits.charAt(5) != '6') {
                throw new IllegalArgumentException("Invalid Cameroon phone number prefix: must start with 6");
            }
            return "+237" + digits.substring(5);
        }

        // 4. Invalid length or unsupported format
        if (length < 9) {
            throw new IllegalArgumentException("Phone number is too short");
        } else {
            throw new IllegalArgumentException("Phone number has an invalid length or format");
        }
    }
}
