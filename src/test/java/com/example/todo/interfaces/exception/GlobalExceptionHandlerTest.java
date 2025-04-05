package com.example.todo.interfaces.exception;

import com.example.todo.domain.exception.TodoNotFoundException;
import com.example.todo.domain.exception.TodoValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    @DisplayName("TodoNotFoundException을 처리하면 404 상태 코드와 에러 메시지를 반환해야 한다")
    void handleTodoNotFoundException() {
        // given
        Long id = 1L;
        TodoNotFoundException exception = new TodoNotFoundException(id);

        // when
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTodoNotFoundException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getBody().getMessage()).contains(id.toString());
    }

    @Test
    @DisplayName("TodoValidationException을 처리하면 400 상태 코드와 에러 메시지를 반환해야 한다")
    void handleTodoValidationException() {
        // given
        String errorMessage = "유효하지 않은 입력";
        TodoValidationException exception = new TodoValidationException(errorMessage);

        // when
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTodoValidationException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getBody().getMessage()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("MethodArgumentNotValidException을 처리하면 400 상태 코드와 필드별 에러 메시지를 반환해야 한다")
    void handleValidationExceptions() {
        // given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("todoRequest", "title", "제목은 필수입니다"));
        fieldErrors.add(new FieldError("todoRequest", "description", "설명은 1000자를 초과할 수 없습니다"));

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // when
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).containsKey("title");
        assertThat(response.getBody()).containsKey("description");
    }

    @Test
    @DisplayName("예상치 못한 예외를 처리하면 500 상태 코드와 일반적인 에러 메시지를 반환해야 한다")
    void handleGlobalException() {
        // given
        Exception exception = new RuntimeException("예상치 못한 오류");

        // when
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGlobalException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred");
    }
} 