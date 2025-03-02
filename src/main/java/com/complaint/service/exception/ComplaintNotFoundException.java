package com.complaint.service.exception;

import org.springframework.http.HttpStatus;

public class ComplaintNotFoundException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public ComplaintNotFoundException(String message) {
        super(message);
    }
}
