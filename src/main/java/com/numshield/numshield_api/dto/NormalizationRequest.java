package com.numshield.numshield_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Request payload for phone number normalization.
 */
@Data
public class NormalizationRequest {

    @Schema(
        description = "The raw phone number to be normalized. Supports local, country-code with or without +, and 00 prefixes.",
        example = "690123456",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String phoneNumber;
}
