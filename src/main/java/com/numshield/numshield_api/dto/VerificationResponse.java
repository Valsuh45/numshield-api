package com.numshield.numshield_api.dto;

import com.numshield.numshield_api.operator.TelecomOperator;
import io.swagger.v3.oas.annotations.media.Schema;

public record VerificationResponse(
        @Schema(example = "690123456") String input,
        @Schema(example = "+237690123456") String normalized,
        @Schema(example = "true") boolean valid,
        @Schema(example = "ORANGE") TelecomOperator operator,
        @Schema(example = "237") String countryCode,
        @Schema(example = "CM") String country) {
}
