package com.numshield.numshield_api.dto;

import com.numshield.numshield_api.operator.TelecomOperator;
import io.swagger.v3.oas.annotations.media.Schema;

public record VerificationResponse(
        @Schema(example = "690123456") String input,
        @Schema(example = "+237690123456") String normalized,
        @Schema(example = "true", description = "Structural validity only; does not prove reachability or ownership") boolean valid,
        @Schema(example = "ORANGE", description = "Configured prefix allocation, not a live serving-network lookup") TelecomOperator operator,
        @Schema(example = "237") String countryCode,
        @Schema(example = "CM") String country) {
}
