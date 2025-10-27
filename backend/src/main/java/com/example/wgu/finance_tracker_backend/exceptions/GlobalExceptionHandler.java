package com.example.wgu.finance_tracker_backend.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles resource not found (e.g., account ID not found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An unexpected error occurred"));
    }


    /**
     * ⭐️ FIX: Maps business logic errors (IllegalStateException from service) and
     * database constraint errors (DataIntegrityViolationException) to a 409 Conflict.
     * This ensures the user sees a clear, descriptive error message.
     */
    @ExceptionHandler({IllegalStateException.class, DataIntegrityViolationException.class})
    public ResponseEntity<String> handleConflictOrConstraintViolation(Exception ex, WebRequest request) {
        String message = ex.getMessage();

        // If the exception is a raw DataIntegrityViolation, provide a helpful default message
        if (ex instanceof DataIntegrityViolationException) {
            message = "Cannot complete the request due to dependent data. Please delete all related transactions or goals first.";
        }

        return new ResponseEntity<>(message, HttpStatus.CONFLICT); // 409
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException ex) {
        System.out.println("Validation error: " + ex.getMessage());
        return ResponseEntity.badRequest().body("Validation failed");
    }

}
