package com.example.employeemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler
 *
 * This class handles ALL exceptions across the entire application in one place.
 * Without this, Spring would return ugly, confusing error responses.
 * With this, we return clean, readable JSON error messages.
 *
 * @RestControllerAdvice tells Spring:
 * "This class intercepts exceptions from all controllers and handles them."
 *
 * @ExceptionHandler(SomeException.class) marks a method to handle a specific exception type.
 *
 * EXAMPLE - without GlobalExceptionHandler, an error looks like:
 * {
 *   "timestamp": "2024-01-01T10:00:00",
 *   "status": 500,
 *   "error": "Internal Server Error",
 *   ... lots of confusing stuff ...
 * }
 *
 * WITH GlobalExceptionHandler, our error looks like:
 * {
 *   "status": 404,
 *   "message": "Employee not found with id: 99",
 *   "timestamp": "2024-01-01T10:00:00"
 * }
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle ResourceNotFoundException (404 Not Found)
     *
     * Triggered when an employee ID doesn't exist in the database.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {

        // Build a clean error response map
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());       // 404
        errorResponse.put("error", "Not Found");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now());

        // Return the response with 404 status
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle Validation Errors (400 Bad Request)
     *
     * Triggered when @Valid fails - e.g., empty name, invalid email, negative salary.
     * Spring throws MethodArgumentNotValidException when validation annotations fail.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {

        // Collect all field-level validation errors
        // Example: { "name": "Name is required", "email": "Please provide a valid email" }
        Map<String, String> fieldErrors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        // Build the full error response
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());     // 400
        errorResponse.put("error", "Validation Failed");
        errorResponse.put("message", "Please fix the following errors:");
        errorResponse.put("fieldErrors", fieldErrors);
        errorResponse.put("timestamp", LocalDateTime.now());

        // Return the response with 400 status
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle any other unexpected exceptions (500 Internal Server Error)
     *
     * This is a catch-all for any exception we didn't specifically handle.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // 500
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
