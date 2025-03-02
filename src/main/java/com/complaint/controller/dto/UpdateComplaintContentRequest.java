package com.complaint.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateComplaintContentRequest(
        @NotBlank(message = "Content cannot be blank")
        String content) {
}
