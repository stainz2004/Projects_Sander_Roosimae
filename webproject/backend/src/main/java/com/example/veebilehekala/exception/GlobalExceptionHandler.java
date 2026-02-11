package com.example.veebilehekala.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplication(
            ApplicationException ex) {
        // Use the status code from the exception
        ErrorResponse error = new ErrorResponse(
                ex.getStatusCode(), ex.getMessage(), Instant.now());
        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleEntityNotFound(EntityNotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex) {
        // 500 Internal Server Error for unexpected errors
        log.error("Unexpected error", ex);
        ErrorResponse error = new ErrorResponse(
                500, "Internal server error", Instant.now());
        return ResponseEntity.status(500).body(error);
    }
}
