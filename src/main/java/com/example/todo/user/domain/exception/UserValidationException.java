package com.example.todo.user.domain.exception;

/**
 * 사용자 유효성 검사 예외
 */
public class UserValidationException extends RuntimeException {
    
    public UserValidationException(String message) {
        super(message);
    }
} 