package com.example.todo.todo.domain.exception;

public class TodoNotFoundException extends RuntimeException {
    
    public TodoNotFoundException(Long id) {
        super("Todo not found with id: " + id);
    }
} 