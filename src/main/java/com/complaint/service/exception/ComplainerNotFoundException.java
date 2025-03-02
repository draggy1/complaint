package com.complaint.service.exception;

public class ComplainerNotFoundException extends RuntimeException {
    public ComplainerNotFoundException(String message) {
        super(message);
    }
}
