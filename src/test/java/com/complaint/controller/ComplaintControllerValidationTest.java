package com.complaint.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComplaintController.class)
@Import(MockServiceConfig.class)
class ComplaintControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn400WhenContentIsMissing() throws Exception {
        // given
        String invalidRequest = "{}";

        // when & then
        mockMvc.perform(patch("/complaint/1/content")
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
                        "content": ""
                    }
                """;
        patch("/complaint/1/content")
                .contentType(MediaType.APPLICATION_JSON);

        // when & then
        mockMvc.perform(patch("/complaint/1/content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Content cannot be blank"));
    }
}