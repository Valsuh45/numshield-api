package com.numshield.numshield_api.controller;

import com.numshield.numshield_api.exception.GlobalExceptionHandler;
import com.numshield.numshield_api.service.PhoneVerificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PhoneVerificationControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                        new PhoneVerificationController(new PhoneVerificationService()))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldVerifyAndEnrichPhoneNumber() throws Exception {
        mockMvc.perform(post("/api/v1/number/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneNumber\":\"690 12 34 56\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.input").value("690 12 34 56"))
                .andExpect(jsonPath("$.data.normalized").value("+237690123456"))
                .andExpect(jsonPath("$.data.valid").value(true))
                .andExpect(jsonPath("$.data.operator").value("ORANGE"))
                .andExpect(jsonPath("$.data.countryCode").value("237"))
                .andExpect(jsonPath("$.data.country").value("CM"));
    }

    @Test
    void shouldReturnStandardErrorForInvalidNumber() throws Exception {
        mockMvc.perform(post("/api/v1/number/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneNumber\":\"not-a-number\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_PHONE_NUMBER"))
                .andExpect(jsonPath("$.error.stage").value("NORMALIZATION"));
    }

    @Test
    void shouldRejectMissingPhoneNumber() throws Exception {
        mockMvc.perform(post("/api/v1/number/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_REQUEST"));
    }

    @Test
    void shouldRejectMalformedJsonWithStandardError() throws Exception {
        mockMvc.perform(post("/api/v1/number/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not-json}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.error.stage").value("REQUEST_VALIDATION"));
    }
}
