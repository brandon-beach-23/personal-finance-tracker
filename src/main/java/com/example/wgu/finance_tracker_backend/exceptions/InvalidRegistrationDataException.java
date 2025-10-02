package com.example.wgu.finance_tracker_backend.exceptions;

public class InvalidRegistrationDataException extends RuntimeException {
    public InvalidRegistrationDataException(String message) {
        super(message);
    }
}
