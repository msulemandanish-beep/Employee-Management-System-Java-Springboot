package com.example.employeemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceNotFoundException
 *
 * This is a CUSTOM EXCEPTION we create to handle "not found" scenarios.
 *
 * Example: If someone requests GET /api/employees/999 but employee with
 * ID 999 doesn't exist, we throw this exception.
 *
 * @ResponseStatus(HttpStatus.NOT_FOUND) tells Spring to automatically
 * return a 404 HTTP status code when this exception is thrown.
 *
 * By extending RuntimeException:
 * - We don't need to declare it in method signatures with "throws"
 * - Spring can automatically handle it via our GlobalExceptionHandler
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor that accepts a custom error message.
     *
     * Example usage:
     *   throw new ResourceNotFoundException("Employee not found with id: 5");
     *
     * @param message - descriptive error message
     */
    public ResourceNotFoundException(String message) {
        // Pass the message to the parent RuntimeException class
        super(message);
    }

}
