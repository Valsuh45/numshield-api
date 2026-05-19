package com.numshield.numshield_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Success response for phone number validation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponse {

    @Schema(
        description = "The normalized version of the validated phone number",
        example = "+237690123456"
    )
    private String phoneNumber;

    @Schema(
        description = "True if the phone number is valid according to Cameroon standards, false otherwise",
        example = "true"
    )
    private boolean valid;
}
