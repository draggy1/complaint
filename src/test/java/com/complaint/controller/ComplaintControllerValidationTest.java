package com.complaint.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComplaintController.class)
@Import(MockServiceConfig.class)
class ComplaintControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn400WhenProductIdIsMissing() throws Exception {
        // given
        String invalidRequest = """
                {
                    "complainerId": 1,
                    "content": "Valid content",
                    "complaintCountry": "Poland"
                }
                """;

        // when & then
        mockMvc.perform(post("/complaint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Product ID cannot be null"));
    }

    @Test
    void shouldReturn400WhenComplainerIdIsMissing() throws Exception {
        // given
        String invalidRequest = """
                {
                    "productId": 1,
                    "content": "Valid content",
                    "complaintCountry": "Poland"
                }
                """;

        // when & then
        mockMvc.perform(post("/complaint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Complainer ID cannot be null"));
    }

    @Test
    void shouldReturn400WhenContentIsMissing() throws Exception {
        // given
        String invalidRequest = """
                {
                    "productId": 1,
                    "complainerId": 1,
                    "complaintCountry": "Poland"
                }
                """;

        // when & then
        mockMvc.perform(post("/complaint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Content cannot be blank"));
    }

    @Test
    void shouldReturn400WhenContentIsBlank() throws Exception {
        // given
        String invalidRequest = """
                {
                    "productId": 1,
                    "complainerId": 1,
                    "content": "",
                    "complaintCountry": "Poland"
                }
                """;

        // when & then
        mockMvc.perform(post("/complaint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Content cannot be blank"));
    }

    @Test
    void shouldReturn201WhenValidRequestIsSent() throws Exception {
        // given
        String validRequest = """
                {
                    "productId": 1,
                    "complainerId": 1,
                    "content": "Valid content",
                    "complaintCountry": "Poland"
                }
                """;

        // when & then
        mockMvc.perform(post("/complaint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest))
                .andExpect(status().isCreated());
    }
}
