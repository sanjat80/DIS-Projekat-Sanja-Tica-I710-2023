package com.sanjat.enrollment_service.exception;

public class ApplicationFailedException extends RuntimeException {
    public ApplicationFailedException(String message) {
        super(message);
    }
}