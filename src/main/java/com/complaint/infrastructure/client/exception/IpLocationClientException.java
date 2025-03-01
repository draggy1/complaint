package com.complaint.infrastructure.client.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public final class IpLocationClientException extends RuntimeException {

    private final HttpStatus httpStatus = INTERNAL_SERVER_ERROR;
    public IpLocationClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
