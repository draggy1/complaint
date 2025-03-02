package com.complaint.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

public record ComplaintDto(int id,
                           ProductDto product,
                           ComplainerDto complainer,
                           String content,
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
                           OffsetDateTime createdAt,
                           String complaintCountry,
                           int complaintCount) {
}
