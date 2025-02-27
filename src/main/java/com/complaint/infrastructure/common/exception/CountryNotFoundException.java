package com.complaint.infrastructure.common.exception;

import org.springframework.http.HttpStatus;

public class CountryNotFoundException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public CountryNotFoundException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
