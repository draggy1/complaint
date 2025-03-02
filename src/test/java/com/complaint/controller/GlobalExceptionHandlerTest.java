package com.complaint.controller;

import com.complaint.service.ComplaintService;
import com.complaint.service.exception.ComplainerNotFoundException;
import com.complaint.service.exception.ComplaintNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.persistence.OptimisticLockException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComplaintController.class)
@Import({GlobalExceptionHandler.class, MockServiceConfig.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ComplaintService complaintService;

    @Test
    void shouldReturn404WhenComplaintNotFound() throws Exception {
        // given
        when(complaintService.getComplaintById(anyInt()))
                .thenThrow(new ComplaintNotFoundException("Complaint not found"));

        // when & then
        mockMvc.perform(get("/complaint/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Complaint not found"));
    }

    @Test
    void shouldReturn404WhenComplainerNotFound() throws Exception {
        // given
        when(complaintService.addComplaint(any()))
                .thenThrow(new ComplainerNotFoundException("Complainer not found"));

        // when & then
        mockMvc.perform(post("/complaint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "productId": 1,
                                    "complainerId": 999,
                                    "content": "Some complaint",
                                    "complaintCountry": "Poland"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Complainer not found"));
    }

    @Test
    void shouldReturn500WhenDatabaseErrorOccurs() throws Exception {
        // given
        when(complaintService.getAllComplaints())
                .thenThrow(new DataAccessException("Database error") {});

        // when & then
        mockMvc.perform(get("/complaint/all"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("Database error occurred"));
    }

    @Test
    void shouldReturn409WhenOptimisticLockingFails() throws Exception {
        // given
        when(complaintService.updateComplaintContent(anyInt(), anyString()))
                .thenThrow(new OptimisticLockException("Optimistic locking conflict"));

        // when & then
        mockMvc.perform(patch("/complaint/1/content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "content": "Updated content"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value(409))
                .andExpect(jsonPath("$.message")
                        .value("Conflict detected: Someone else modified this complaint. Please reload and try again."));
    }

    @Test
    void shouldReturn500ForUnexpectedException() throws Exception {
        // given
        when(complaintService.getComplaintById(anyInt()))
                .thenThrow(new RuntimeException("Unexpected failure"));

        // when & then
        mockMvc.perform(get("/complaint/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("Unexpected error occurred"));
    }

    @BeforeEach
    void resetMocks() {
        Mockito.reset(complaintService);
    }
}