package com.numshield.numshield_api.controller;

import com.numshield.numshield_api.dto.ErrorResponse;
import com.numshield.numshield_api.dto.NormalizationRequest;
import com.numshield.numshield_api.dto.NormalizationResponse;
import com.numshield.numshield_api.util.CameroonPhoneNumberNormalizer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller exposing endpoints for phone number operations.
 */
@RestController
@RequestMapping("/api/v1/phone-numbers")
@Tag(name = "Phone Number Normalization", description = "Endpoints for validating and normalizing Cameroon phone numbers")
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
    public ResponseEntity<?> normalizePost(@RequestBody NormalizationRequest request) {
        if (request == null || request.getPhoneNumber() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Missing 'phoneNumber' field in request body"));
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
    public ResponseEntity<?> normalizeGet(
            @Parameter(description = "The raw phone number to be normalized", example = "690123456", required = true)
            @RequestParam("number") String phoneNumber) {
        return processNormalization(phoneNumber);
    }

    private ResponseEntity<?> processNormalization(String phoneNumber) {
        try {
            String normalized = CameroonPhoneNumberNormalizer.normalize(phoneNumber);
            return ResponseEntity.ok(NormalizationResponse.builder()
                    .raw(phoneNumber == null ? "" : phoneNumber)
                    .normalized(normalized)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}
