package com.example.todo.domain.exception;

public class TodoValidationException extends RuntimeException {
    
    public TodoValidationException(String message) {
        super(message);
    }
} 