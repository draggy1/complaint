package com.complaint.infrastructure.controller;

import com.complaint.infrastructure.common.exception.CountryNotFoundException;
import com.complaint.infrastructure.common.exception.IpLocationClientException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IpLocationClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIpLocationException(IpLocationClientException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(CountryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCountryNotFoundException(CountryNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }
}

record ErrorResponse(String message) {}
