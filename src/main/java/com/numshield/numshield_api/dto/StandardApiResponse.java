package com.numshield.numshield_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StandardApiResponse<T>(
        @Schema(example = "true") boolean success,
        T data,
        ApiError error,
        @Schema(example = "2026-10-02T08:00:00Z") Instant timestamp) {

    public static <T> StandardApiResponse<T> success(T data) {
        return new StandardApiResponse<>(true, data, null, Instant.now());
    }

    public static StandardApiResponse<Void> error(String code, String message, String stage) {
        return new StandardApiResponse<>(false, null, new ApiError(code, message, stage), Instant.now());
    }
}
