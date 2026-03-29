package org.example.backend.exception;

/**
 * An exception that is thrown when there is no loan available for the customer based on the provided data.
 */
public class NoLoanException extends RuntimeException {
    public NoLoanException(String message) {
        super(message);
    }
}
