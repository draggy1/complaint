package com.complaint.service.exception;

public class ComplaintNotFoundException extends RuntimeException {

    public ComplaintNotFoundException(String message) {
        super(message);
    }
}
