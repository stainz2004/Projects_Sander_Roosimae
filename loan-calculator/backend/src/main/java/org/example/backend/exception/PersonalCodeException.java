package org.example.backend.exception;

/**
 * An exception that is thrown when the personal code provided in the request is invalid.
 */
public class PersonalCodeException extends RuntimeException {
    public PersonalCodeException(String message) {
        super(message);
    }
}
