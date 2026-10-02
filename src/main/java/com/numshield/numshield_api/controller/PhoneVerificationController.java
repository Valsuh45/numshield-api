package com.numshield.numshield_api.controller;

import com.numshield.numshield_api.dto.StandardApiResponse;
import com.numshield.numshield_api.dto.VerificationRequest;
import com.numshield.numshield_api.dto.VerificationResponse;
import com.numshield.numshield_api.service.PhoneVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/number")
@Tag(name = "Phone Number Verification")
public class PhoneVerificationController {

    private final PhoneVerificationService verificationService;

    public PhoneVerificationController(PhoneVerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping(value = "/verify", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Verify and enrich a Cameroon phone number")
    public ResponseEntity<StandardApiResponse<VerificationResponse>> verify(
            @Valid @RequestBody VerificationRequest request) {
        return ResponseEntity.ok(StandardApiResponse.success(verificationService.verify(request.getPhoneNumber())));
    }
}
