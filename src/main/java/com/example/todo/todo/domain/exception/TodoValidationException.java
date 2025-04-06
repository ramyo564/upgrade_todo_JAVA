package com.example.todo.todo.domain.exception;

public class TodoValidationException extends RuntimeException {
    
    public TodoValidationException(String message) {
        super(message);
    }
} 