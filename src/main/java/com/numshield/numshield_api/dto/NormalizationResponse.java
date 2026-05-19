package com.numshield.numshield_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * Success response for phone number normalization.
 */
@Data
@Builder
public class NormalizationResponse {

    @Schema(
        description = "The original raw phone number input provided by the client",
        example = "690 12 34 56"
    )
    private String raw;

    @Schema(
        description = "The normalized phone number in standard Cameroon format (+237XXXXXXXXX)",
        example = "+237690123456"
    )
    private String normalized;
}
