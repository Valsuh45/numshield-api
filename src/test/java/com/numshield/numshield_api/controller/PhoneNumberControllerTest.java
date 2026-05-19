package com.numshield.numshield_api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PhoneNumberControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PhoneNumberController()).build();
    }

    @Test
    void shouldNormalizePhoneNumberViaGet() throws Exception {
        mockMvc.perform(get("/api/v1/phone-numbers/normalize")
                        .param("number", "690123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.raw").value("690123456"))
                .andExpect(jsonPath("$.normalized").value("+237690123456"));
    }

    @Test
    void shouldReturnBadRequestForInvalidNumberViaGet() throws Exception {
        mockMvc.perform(get("/api/v1/phone-numbers/normalize")
                        .param("number", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void shouldNormalizePhoneNumberViaPost() throws Exception {
        mockMvc.perform(post("/api/v1/phone-numbers/normalize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneNumber\":\"690123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.raw").value("690123456"))
                .andExpect(jsonPath("$.normalized").value("+237690123456"));
    }

    @Test
    void shouldReturnBadRequestForInvalidNumberViaPost() throws Exception {
        mockMvc.perform(post("/api/v1/phone-numbers/normalize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneNumber\":\"invalid\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
