package com.numshield.numshield_api.exception;

import com.numshield.numshield_api.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global exception handler for all API controllers.
 * Tags each error response with the pipeline stage where the failure occurred.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation-specific failures (Cameroon numbering rules).
     */
    @ExceptionHandler(PhoneNumberValidationException.class)
    public ResponseEntity<ErrorResponse> handlePhoneNumberValidationException(PhoneNumberValidationException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "VALIDATION"));
    }

    /**
     * Handles normalization failures (unrecognized format, invalid characters, wrong length before normalization).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "NORMALIZATION"));
    }

    /**
     * Handles JSR-380 @Valid constraint violations (e.g. @NotBlank, @ValidCameroonPhone on DTOs).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(new ErrorResponse(errorMsg, "NORMALIZATION"));
    }
}
