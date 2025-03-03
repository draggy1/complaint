package com.complaint.infrastructure.client.exception;

import lombok.Getter;

@Getter
public class CountryNotFoundException extends RuntimeException {
    public CountryNotFoundException(String message) {
        super(message);
    }
}
