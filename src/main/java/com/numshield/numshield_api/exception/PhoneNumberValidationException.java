package com.numshield.numshield_api.exception;

/**
 * Exception thrown when a phone number fails Cameroon numbering rules validation.
 */
public class PhoneNumberValidationException extends RuntimeException {

    public PhoneNumberValidationException(String message) {
        super(message);
    }
}
