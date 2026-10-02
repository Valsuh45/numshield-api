package com.numshield.numshield_api.exception;

import com.numshield.numshield_api.dto.StandardApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleUnsupportedMethod(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(e.getStatusCode()).headers(e.getHeaders())
                .body(StandardApiResponse.error("INVALID_REQUEST", "HTTP method is not supported for this endpoint", "REQUEST_VALIDATION"));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException e) {
        return ResponseEntity.status(e.getStatusCode()).headers(e.getHeaders())
                .body(StandardApiResponse.error("INVALID_REQUEST", "Request content type is not supported; use application/json", "REQUEST_VALIDATION"));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<StandardApiResponse<Void>> handleMalformedRequest(Exception e) {
        return ResponseEntity.badRequest()
                .body(StandardApiResponse.error("INVALID_REQUEST", "The request payload is missing or malformed", "REQUEST_VALIDATION"));
    }

    /**
     * Handles validation-specific failures (Cameroon numbering rules).
     */
    @ExceptionHandler(PhoneNumberValidationException.class)
    public ResponseEntity<StandardApiResponse<Void>> handlePhoneNumberValidationException(PhoneNumberValidationException e) {
        return ResponseEntity.badRequest()
                .body(StandardApiResponse.error("INVALID_PHONE_NUMBER", e.getMessage(), "VALIDATION"));
    }

    /**
     * Handles normalization failures (unrecognized format, invalid characters, wrong length before normalization).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(StandardApiResponse.error("INVALID_PHONE_NUMBER", e.getMessage(), "NORMALIZATION"));
    }

    /**
     * Handles JSR-380 @Valid constraint violations (e.g. @NotBlank, @ValidCameroonPhone on DTOs).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(StandardApiResponse.error("INVALID_REQUEST", errorMsg, "REQUEST_VALIDATION"));
    }
}
