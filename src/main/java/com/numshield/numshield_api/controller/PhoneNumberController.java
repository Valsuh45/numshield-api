package com.numshield.numshield_api.controller;

import com.numshield.numshield_api.dto.ErrorResponse;
import com.numshield.numshield_api.dto.NormalizationRequest;
import com.numshield.numshield_api.dto.NormalizationResponse;
import com.numshield.numshield_api.dto.ValidationRequest;
import com.numshield.numshield_api.dto.ValidationResponse;
import com.numshield.numshield_api.util.CameroonPhoneNumberNormalizer;
import com.numshield.numshield_api.util.CameroonPhoneNumberValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller exposing endpoints for phone number operations.
 */
@RestController
@RequestMapping("/api/v1/phone-numbers")
@Tag(name = "Phone Number Services", description = "Endpoints for normalizing and validating Cameroon phone numbers")
public class PhoneNumberController {

    /**
     * Normalizes a phone number via POST request.
     *
     * @param request the request payload containing the phone number
     * @return the normalized phone number or an error message
     */
    @PostMapping(value = "/normalize", consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Normalize Cameroon phone number (POST)",
            description = "Validates and normalizes any supported Cameroon phone number format into the standardized '+237XXXXXXXXX' format.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Phone number successfully validated and normalized",
                            content = @Content(schema = @Schema(implementation = NormalizationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Malformed input or validation failure",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<NormalizationResponse> normalizePost(@RequestBody NormalizationRequest request) {
        if (request == null || request.getPhoneNumber() == null) {
            throw new IllegalArgumentException("Missing 'phoneNumber' field in request body");
        }
        return processNormalization(request.getPhoneNumber());
    }

    /**
     * Normalizes a phone number via GET request.
     *
     * @param phoneNumber the query parameter "number"
     * @return the normalized phone number or an error message
     */
    @GetMapping(value = "/normalize", produces = "application/json")
    @Operation(
            summary = "Normalize Cameroon phone number (GET)",
            description = "Validates and normalizes any supported Cameroon phone number format into the standardized '+237XXXXXXXXX' format.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Phone number successfully validated and normalized",
                            content = @Content(schema = @Schema(implementation = NormalizationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Malformed input or validation failure",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<NormalizationResponse> normalizeGet(
            @Parameter(description = "The raw phone number to be normalized", example = "690123456", required = true)
            @RequestParam("number") String phoneNumber) {
        return processNormalization(phoneNumber);
    }

    /**
     * Validates a phone number via POST request.
     *
     * @param request the request payload containing the phone number
     * @return the validation details or an error message
     */
    @PostMapping(value = "/validate", consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Validate Cameroon phone number (POST)",
            description = "Validates any Cameroon phone number format using JSR-380 annotations and Cameroon numbering rules.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Phone number is valid",
                            content = @Content(schema = @Schema(implementation = ValidationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid phone number format or value",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<ValidationResponse> validatePost(@Valid @RequestBody ValidationRequest request) {
        // 1. Normalization
        String normalized = CameroonPhoneNumberNormalizer.normalize(request.getPhoneNumber());
        // 2. Explicit validation after normalization (AC: validation occurs after normalization)
        CameroonPhoneNumberValidator.validate(normalized);
        return ResponseEntity.ok(new ValidationResponse(normalized, true));
    }

    /**
     * Validates a phone number via GET request.
     *
     * @param phoneNumber the query parameter "number"
     * @return the validation details or an error message
     */
    @GetMapping(value = "/validate", produces = "application/json")
    @Operation(
            summary = "Validate Cameroon phone number (GET)",
            description = "Validates any Cameroon phone number format and returns whether it is valid or not.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Phone number is valid",
                            content = @Content(schema = @Schema(implementation = ValidationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid phone number format or value",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<ValidationResponse> validateGet(
            @Parameter(description = "The phone number to validate", example = "690123456", required = true)
            @RequestParam("number") String phoneNumber) {
        String normalized = CameroonPhoneNumberNormalizer.normalize(phoneNumber);
        CameroonPhoneNumberValidator.validate(normalized);
        return ResponseEntity.ok(new ValidationResponse(normalized, true));
    }

    private ResponseEntity<NormalizationResponse> processNormalization(String phoneNumber) {
        // 1. Normalization
        String normalized = CameroonPhoneNumberNormalizer.normalize(phoneNumber);

        // 2. Validation
        CameroonPhoneNumberValidator.validate(normalized);

        return ResponseEntity.ok(NormalizationResponse.builder()
                .raw(phoneNumber)
                .normalized(normalized)
                .build());
    }
}
