package com.complaint.controller;

import com.complaint.common.ErrorResult;
import com.complaint.infrastructure.client.exception.CountryNotFoundException;
import com.complaint.infrastructure.client.exception.IpLocationClientException;
import com.complaint.service.exception.ComplainerNotFoundException;
import com.complaint.service.exception.ComplaintNotFoundException;
import com.complaint.service.exception.ProductNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ComplaintNotFoundException.class)
    public ResponseEntity<ErrorResult> handleComplaintNotFound(ComplaintNotFoundException e) {
        log.warn("Complaint not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(ComplainerNotFoundException.class)
    public ResponseEntity<ErrorResult> handleComplainerNotFound(ComplainerNotFoundException e) {
        log.warn("Complainer not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResult> handleProductNotFound(ProductNotFoundException e) {
        log.warn("Product not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }


    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<ErrorResult> handleCountryNotFoundException(CountryNotFoundException e) {
        log.warn("Country not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(IpLocationClientException.class)
    public ResponseEntity<ErrorResult> handleIpLocationException(IpLocationClientException e) {
        log.error("IP location client error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResult> handleDatabaseException(DataAccessException e) {
        log.error("Database error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Database error occurred"));
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ErrorResult> handleOptimisticLockException(OptimisticLockException e) {
        log.warn("Optimistic locking failure: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResult(
                        HttpStatus.CONFLICT.value(),
                        "Conflict detected: Someone else modified this complaint. Please reload and try again.")
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest()
                .body(new ErrorResult(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> handleGenericException(Exception e) {
        log.error("Unexpected error occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error occurred"));
    }
}
