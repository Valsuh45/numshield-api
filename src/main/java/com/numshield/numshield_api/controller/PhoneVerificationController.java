package com.numshield.numshield_api.controller;

import com.numshield.numshield_api.dto.StandardApiResponse;
import com.numshield.numshield_api.dto.VerificationRequest;
import com.numshield.numshield_api.dto.VerificationResponse;
import com.numshield.numshield_api.service.PhoneVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/number")
@ApiResponse(responseCode = "405", description = "HTTP method is not supported",
        content = @Content(schema = @Schema(implementation = StandardApiResponse.class)))
@Tag(name = "Phone Number Verification")
public class PhoneVerificationController {

    private final PhoneVerificationService verificationService;

    public PhoneVerificationController(PhoneVerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping(value = "/verify", consumes = "application/json", produces = "application/json")
    @ApiResponse(responseCode = "415", description = "Request content type is not supported",
            content = @Content(schema = @Schema(implementation = StandardApiResponse.class)))
    @Operation(summary = "Verify and enrich a Cameroon phone number", responses = {
            @ApiResponse(responseCode = "200", description = "Verification completed", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Invalid request or phone number",
                    content = @Content(schema = @Schema(implementation = StandardApiResponse.class)))
    })
    public ResponseEntity<StandardApiResponse<VerificationResponse>> verify(
            @Valid @RequestBody VerificationRequest request) {
        return ResponseEntity.ok(StandardApiResponse.success(verificationService.verify(request.getPhoneNumber())));
    }
}
