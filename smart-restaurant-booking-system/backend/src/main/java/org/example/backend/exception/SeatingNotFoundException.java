package org.example.backend.exception;

public class SeatingNotFoundException extends RuntimeException {
    public SeatingNotFoundException(String message) {
        super(message);
    }
}