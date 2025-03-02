package com.complaint.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateComplaintRequest(
        @NotNull(message = "Product ID cannot be null")
        Integer productId,

        @NotNull(message = "Complainer ID cannot be null")
        Integer complainerId,

        @NotBlank(message = "Content cannot be blank")
        String content
) {}
