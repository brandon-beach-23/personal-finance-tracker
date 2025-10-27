package com.example.wgu.finance_tracker_backend.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handles resource not found (e.g., account ID not found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); // 404
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

    // Fallback for all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
        // Log the exception details here for debugging
        ex.printStackTrace();
        // Updated message to be more explicit about a server-side failure
        return new ResponseEntity<>("An unexpected internal server error occurred. Please check the server logs for details.", HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException ex) {
        System.out.println("❌ Validation error: " + ex.getMessage());
        return ResponseEntity.badRequest().body("Validation failed");
    }

}
