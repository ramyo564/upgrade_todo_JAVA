package com.example.todo.application.service;

import com.example.todo.todo.domain.exception.TodoNotFoundException;
import com.example.todo.todo.domain.exception.TodoValidationException;
import com.example.todo.todo.domain.model.Todo;
import com.example.todo.todo.domain.repository.TodoRepository;
import com.example.todo.todo.application.service.TodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceExceptionTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    @DisplayName("존재하지 않는 ID로 Todo를 조회하면 TodoNotFoundException이 발생해야 한다")
    void getTodoNotFound() {
        // given
        Long id = 1L;
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        assertThrows(TodoNotFoundException.class, () -> {
            todoService.getTodo(id);
        });
    }

    @Test
    @DisplayName("삭제된 Todo를 조회하면 TodoNotFoundException이 발생해야 한다")
    void getDeletedTodo() {
        // given
        Long id = 1L;
        Todo todo = new Todo("테스트", "설명");
        todo.delete();
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

        // when & then
        assertThrows(TodoNotFoundException.class, () -> {
            todoService.getTodo(id);
        });
    }

    @Test
    @DisplayName("존재하지 않는 ID로 Todo를 업데이트하면 TodoNotFoundException이 발생해야 한다")
    void updateTodoNotFound() {
        // given
        Long id = 1L;
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        assertThrows(TodoNotFoundException.class, () -> {
            todoService.updateTodo(id, "새로운 제목", "새로운 설명");
        });
    }

    @Test
    @DisplayName("존재하지 않는 ID로 Todo를 완료하면 TodoNotFoundException이 발생해야 한다")
    void completeTodoNotFound() {
        // given
        Long id = 1L;
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        assertThrows(TodoNotFoundException.class, () -> {
            todoService.completeTodo(id);
        });
    }

    @Test
    @DisplayName("존재하지 않는 ID로 Todo를 미완료 처리하면 TodoNotFoundException이 발생해야 한다")
    void uncompleteTodoNotFound() {
        // given
        Long id = 1L;
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        assertThrows(TodoNotFoundException.class, () -> {
            todoService.uncompleteTodo(id);
        });
    }

    @Test
    @DisplayName("존재하지 않는 ID로 Todo를 삭제하면 TodoNotFoundException이 발생해야 한다")
    void deleteTodoNotFound() {
        // given
        Long id = 1L;
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        assertThrows(TodoNotFoundException.class, () -> {
            todoService.deleteTodo(id);
        });
    }

    @Test
    @DisplayName("유효하지 않은 제목으로 Todo를 생성하면 TodoValidationException이 발생해야 한다")
    void createTodoWithInvalidTitle() {
        // given
        String invalidTitle = "";

        // when & then
        assertThrows(TodoValidationException.class, () -> {
            todoService.createTodo(invalidTitle, "설명");
        });
        
        // verify that save was never called
        verify(todoRepository, never()).save(any(Todo.class));
    }
} 