package org.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


/**
 * A global exception handler that catches and handles various exceptions thrown by the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions that occur when the request body fails validation checks.
     *
     * @param ex The exception that was thrown.
     * @return A ResponseEntity containing an ErrorResponse with details about the validation error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldError() != null ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "Validation failed";

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles InvalidLoanAmount exceptions that occur when the loan amount provided in the request is outside the defined limits.
     *
     * @param ex The exception that was thrown.
     * @return A ResponseEntity containing an ErrorResponse with details about the invalid loan amount error.
     */
    @ExceptionHandler(InvalidLoanAmount.class)
    public ResponseEntity<ErrorResponse> handleInvalidLoanAmount(InvalidLoanAmount ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles InvalidLoanPeriod exceptions that occur when the loan period provided in the request is outside the defined limits.
     *
     * @param ex The exception that was thrown.
     * @return A ResponseEntity containing an ErrorResponse with details about the invalid loan period error.
     */
    @ExceptionHandler(InvalidLoanPeriod.class)
    public ResponseEntity<ErrorResponse> handleInvalidLoanPeriod(InvalidLoanPeriod ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles PersonalCodeException exceptions that occur when the personal code provided in the request is invalid.
     *
     * @param ex The exception that was thrown.
     * @return A ResponseEntity containing an ErrorResponse with details about the invalid personal code error.
     */
    @ExceptionHandler(PersonalCodeException.class)
    public ResponseEntity<ErrorResponse> handlePersonalCodeException(PersonalCodeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles NoLoanException exceptions that occur when no valid loan is found based on the provided information.
     *
     * @param ex The exception that was thrown.
     * @return A ResponseEntity containing an ErrorResponse with details about the no loan found error.
     */
    @ExceptionHandler(NoLoanException.class)
    public ResponseEntity<ErrorResponse> handleNoLoanException(NoLoanException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles all other exceptions that are not specifically handled by other exception handlers.
     *
     * @param ex The exception that was thrown.
     * @return A ResponseEntity containing an ErrorResponse with details about the unexpected error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}