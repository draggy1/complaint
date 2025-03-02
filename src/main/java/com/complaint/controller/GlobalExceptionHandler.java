package com.complaint.controller;

import com.complaint.common.ErrorResult;
import com.complaint.infrastructure.client.exception.CountryNotFoundException;
import com.complaint.infrastructure.client.exception.IpLocationClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IpLocationClientException.class)
    public ResponseEntity<ErrorResult> handleIpLocationException(IpLocationClientException e) {
        log.error("IP location client error: {}", e.getMessage(), e);
        return status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<ErrorResult> handleCountryNotFoundException(CountryNotFoundException e) {
        log.warn("Country not found: {}", e.getMessage());
        return status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResult> handleDatabaseException(DataAccessException e) {
        log.error("Database error: {}", e.getMessage(), e);
        return status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Database error occurred"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> handleGenericException(Exception e) {
        log.error("Unexpected error occurred", e);
        return status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error occurred"));
    }
}
