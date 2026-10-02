package com.numshield.numshield_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerificationRequest {

    @NotBlank(message = "Phone number cannot be null or empty")
    @Schema(example = "690123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phoneNumber;
}
