package com.numshield.numshield_api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiContractIntegrationTest {

    private static final String[] POST_PATHS = {
            "/api/v1/phone-numbers/normalize", "/api/v1/phone-numbers/validate", "/api/v1/number/verify"
    };

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    static Stream<Arguments> invalidRequests() {
        return Stream.of(POST_PATHS).flatMap(path -> Stream.of(
                "{}", "{\"phoneNumber\":null}", "{\"phoneNumber\":\"\"}", "{\"phoneNumber\":\"   \"}",
                "{\"phoneNumber\":690123456}", "{\"phoneNumber\":690123456.0}", "{\"phoneNumber\":true}",
                "{\"phoneNumber\":[]}", "{\"phoneNumber\":{}}", "{not-json}", "", "null"
        ).map(body -> Arguments.of(path, body)));
    }

    @ParameterizedTest
    @MethodSource("invalidRequests")
    void rejectsInvalidRequestBodiesWithStandardEnvelope(String path, String body) throws Exception {
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.error.stage").value("REQUEST_VALIDATION"))
                .andExpect(jsonPath("$.error.message").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/v1/phone-numbers/normalize", "/api/v1/phone-numbers/validate", "/api/v1/number/verify"})
    void rejectsUnsupportedMediaTypeWithStandardEnvelope(String path) throws Exception {
        mockMvc.perform(post(path).contentType(MediaType.TEXT_PLAIN).content("690123456"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(header().string("Accept", containsString("application/json")))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.error.stage").value("REQUEST_VALIDATION"))
                .andExpect(jsonPath("$.timestamp").isString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/v1/phone-numbers/normalize", "/api/v1/phone-numbers/validate", "/api/v1/number/verify"})
    void rejectsUnsupportedMethodWithStandardEnvelope(String path) throws Exception {
        mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content("{\"phoneNumber\":\"690123456\"}"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(header().string("Allow", containsString("POST")))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.error.stage").value("REQUEST_VALIDATION"))
                .andExpect(jsonPath("$.timestamp").isString());
    }

    @ParameterizedTest
    @CsvSource({"690123456,ORANGE", "650123456,MTN", "660123456,NEXTTEL", "620123456,CAMTEL", "680123456,UNKNOWN"})
    void verifiesStringInputsAndPrefixAllocation(String number, String operator) throws Exception {
        mockMvc.perform(post("/api/v1/number/verify").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneNumber\":\"" + number + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.normalized").value("+237" + number))
                .andExpect(jsonPath("$.data.valid").value(true))
                .andExpect(jsonPath("$.data.operator").value(operator))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    void generatedOpenApiDescribesWrappedResponsesAndErrors() throws Exception {
        JsonNode api = jsonMapper.readTree(mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        for (String path : POST_PATHS) {
            String payload = path.endsWith("normalize") ? "NormalizationResponse"
                    : path.endsWith("validate") ? "ValidationResponse" : "VerificationResponse";
            String[] methods = path.endsWith("verify") ? new String[]{"post"} : new String[]{"get", "post"};
            for (String method : methods) {
                JsonNode responses = api.path("paths").path(path).path(method).path("responses");
                JsonNode success = resolveResponse(api, responses.path("200"));
                assertThat(success.path("properties").has("success")).as(path + " " + method).isTrue();
                assertThat(success.path("properties").path("data").path("$ref").asString())
                        .isEqualTo("#/components/schemas/" + payload);
                assertThat(success.path("properties").has("timestamp")).isTrue();
                String[] statuses = method.equals("post") ? new String[]{"400", "405", "415"} : new String[]{"400", "405"};
                for (String status : statuses) {
                    JsonNode error = resolveResponse(api, responses.path(status));
                    assertThat(error.path("properties").has("success")).isTrue();
                    assertThat(error.path("properties").path("error").path("$ref").asString())
                            .isEqualTo("#/components/schemas/ApiError");
                }
            }
        }
    }

    @Test
    void staticOpenApiDescribesTheSamePayloadsAndErrorStatuses() throws Exception {
        JsonNode api;
        try (var input = Files.newInputStream(Path.of("openapi/openapi.yaml"))) {
            Object document = new Yaml(new SafeConstructor(new LoaderOptions())).load(input);
            api = jsonMapper.valueToTree(document);
        }
        for (String path : POST_PATHS) {
            String payload = path.endsWith("normalize") ? "NormalizationResponse"
                    : path.endsWith("validate") ? "ValidationResponse" : "VerificationResponse";
            String[] methods = path.endsWith("verify") ? new String[]{"post"} : new String[]{"get", "post"};
            for (String method : methods) {
                JsonNode responses = api.path("paths").path(path).path(method).path("responses");
                JsonNode success = resolveResponse(api, responses.path("200"));
                assertThat(success.at("/allOf/0/$ref").asString()).isEqualTo("#/components/schemas/SuccessEnvelope");
                assertThat(success.at("/allOf/1/properties/data/$ref").asString())
                        .isEqualTo("#/components/schemas/" + payload);
                String[] statuses = method.equals("post") ? new String[]{"400", "405", "415"} : new String[]{"400", "405"};
                for (String status : statuses) {
                    JsonNode response = api.at(responses.path(status).path("$ref").asString().substring(1));
                    assertThat(resolveResponse(api, response).path("properties").path("error").path("$ref").asString())
                            .isEqualTo("#/components/schemas/ApiError");
                }
            }
        }
        assertThat(api.at("/components/schemas/PhoneNumberRequest/properties/phoneNumber/type").asString()).isEqualTo("string");
    }

    private JsonNode resolveResponse(JsonNode api, JsonNode response) {
        String reference = response.path("content").path("application/json").path("schema").path("$ref").asString();
        assertThat(reference).startsWith("#/components/schemas/");
        return api.at(reference.substring(1));
    }
}
