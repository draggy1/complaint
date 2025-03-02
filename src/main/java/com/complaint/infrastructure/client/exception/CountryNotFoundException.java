package com.complaint.infrastructure.client.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CountryNotFoundException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public CountryNotFoundException(String message) {
        super(message);
    }

}
