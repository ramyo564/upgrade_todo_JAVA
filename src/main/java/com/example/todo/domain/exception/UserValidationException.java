package com.example.todo.domain.exception;

/**
 * 사용자 유효성 검사 예외
 */
public class UserValidationException extends RuntimeException {
    
    public UserValidationException(String message) {
        super(message);
    }
} 