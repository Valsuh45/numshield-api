package com.numshield.numshield_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error response payload.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    @Schema(
        description = "Descriptive validation error message explaining why the normalization failed",
        example = "Phone number contains invalid characters"
    )
    private String error;
}
