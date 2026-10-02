package com.numshield.numshield_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ApiError(
        @Schema(example = "INVALID_PHONE_NUMBER") String code,
        @Schema(example = "Phone number contains invalid characters") String message,
        @Schema(example = "NORMALIZATION") String stage) {
}
