package org.example.backend.exception;

/**
 * An exception that is thrown when the loan amount provided in the request is outside the defined limits.
 */
public class InvalidLoanPeriod extends RuntimeException {
    public InvalidLoanPeriod(String message) {
        super(message);
    }
}
