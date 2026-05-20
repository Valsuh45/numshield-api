package com.numshield.numshield_api.dto;

import com.numshield.numshield_api.validation.ValidCameroonPhone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request payload for phone number validation.
 */
@Data
public class ValidationRequest {

    @NotBlank(message = "Phone number cannot be null or empty")
    @ValidCameroonPhone
    @Schema(
        description = "The raw or normalized phone number to validate against Cameroon standards",
        example = "690123456",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String phoneNumber;
}
