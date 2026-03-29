package org.example.backend.exception;

/**
 * An exception that is thrown when the loan amount provided in the request is outside the defined limits.
 */
public class InvalidLoanAmount extends RuntimeException {
    public InvalidLoanAmount(String message) {
        super(message);
    }
}
