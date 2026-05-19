package com.numshield.numshield_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error response payload, indicating where in the pipeline the failure occurred.
 */
@Data
@NoArgsConstructor
public class ErrorResponse {

    @Schema(
        description = "Descriptive error message explaining why the request failed",
        example = "Phone number is too short"
    )
    private String error;

    @Schema(
        description = "The processing stage at which the failure occurred: NORMALIZATION or VALIDATION",
        example = "NORMALIZATION",
        allowableValues = {"NORMALIZATION", "VALIDATION"}
    )
    private String stage;

    /** Convenience constructor for simple error messages (no stage). */
    public ErrorResponse(String error) {
        this.error = error;
        this.stage = null;
    }

    /** Constructor with stage context. */
    public ErrorResponse(String error, String stage) {
        this.error = error;
        this.stage = stage;
    }
}
