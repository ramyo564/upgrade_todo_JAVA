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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServicePatchTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    @DisplayName("제목만 업데이트할 수 있어야 한다")
    void patchTodoWithTitleOnly() {
        // given
        Long id = 1L;
        String newTitle = "Updated Title";
        Todo existingTodo = new Todo("Original Title", "Original Description");
        when(todoRepository.findById(id)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(existingTodo);

        // when
        Todo patchedTodo = todoService.patchTodo(id, newTitle, null);

        // then
        assertThat(patchedTodo.getTitle()).isEqualTo(newTitle);
        assertThat(patchedTodo.getDescription()).isEqualTo("Original Description");
        verify(todoRepository).save(existingTodo);
    }

    @Test
    @DisplayName("설명만 업데이트할 수 있어야 한다")
    void patchTodoWithDescriptionOnly() {
        // given
        Long id = 1L;
        String newDescription = "Updated Description";
        Todo existingTodo = new Todo("Original Title", "Original Description");
        when(todoRepository.findById(id)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(existingTodo);

        // when
        Todo patchedTodo = todoService.patchTodo(id, null, newDescription);

        // then
        assertThat(patchedTodo.getTitle()).isEqualTo("Original Title");
        assertThat(patchedTodo.getDescription()).isEqualTo(newDescription);
        verify(todoRepository).save(existingTodo);
    }

    @Test
    @DisplayName("제목과 설명 모두 업데이트할 수 있어야 한다")
    void patchTodoWithTitleAndDescription() {
        // given
        Long id = 1L;
        String newTitle = "Updated Title";
        String newDescription = "Updated Description";
        Todo existingTodo = new Todo("Original Title", "Original Description");
        when(todoRepository.findById(id)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(existingTodo);

        // when
        Todo patchedTodo = todoService.patchTodo(id, newTitle, newDescription);

        // then
        assertThat(patchedTodo.getTitle()).isEqualTo(newTitle);
        assertThat(patchedTodo.getDescription()).isEqualTo(newDescription);
        verify(todoRepository).save(existingTodo);
    }

    @Test
    @DisplayName("존재하지 않는 Todo를 패치하려고 하면 TodoNotFoundException이 발생해야 한다")
    void patchNonExistentTodo() {
        // given
        Long id = 1L;
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> todoService.patchTodo(id, "New Title", "New Description"))
                .isInstanceOf(TodoNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    @DisplayName("삭제된 Todo를 패치하려고 하면 TodoNotFoundException이 발생해야 한다")
    void patchDeletedTodo() {
        // given
        Long id = 1L;
        Todo deletedTodo = new Todo("Original Title", "Original Description");
        deletedTodo.delete();
        when(todoRepository.findById(id)).thenReturn(Optional.of(deletedTodo));

        // when & then
        assertThatThrownBy(() -> todoService.patchTodo(id, "New Title", "New Description"))
                .isInstanceOf(TodoNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    @DisplayName("빈 제목으로 패치하려고 하면 TodoValidationException이 발생해야 한다")
    void patchWithEmptyTitle() {
        // given
        Long id = 1L;
        Todo existingTodo = new Todo("Original Title", "Original Description");
        when(todoRepository.findById(id)).thenReturn(Optional.of(existingTodo));

        // when & then
        assertThatThrownBy(() -> todoService.patchTodo(id, "", "New Description"))
                .isInstanceOf(TodoValidationException.class)
                .hasMessageContaining("Title cannot be empty");
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    @DisplayName("너무 긴 제목으로 패치하려고 하면 TodoValidationException이 발생해야 한다")
    void patchWithTooLongTitle() {
        // given
        Long id = 1L;
        Todo existingTodo = new Todo("Original Title", "Original Description");
        when(todoRepository.findById(id)).thenReturn(Optional.of(existingTodo));
        String tooLongTitle = "a".repeat(101);

        // when & then
        assertThatThrownBy(() -> todoService.patchTodo(id, tooLongTitle, "New Description"))
                .isInstanceOf(TodoValidationException.class)
                .hasMessageContaining("Title cannot be longer than 100 characters");
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    @DisplayName("너무 긴 설명으로 패치하려고 하면 TodoValidationException이 발생해야 한다")
    void patchWithTooLongDescription() {
        // given
        Long id = 1L;
        Todo existingTodo = new Todo("Original Title", "Original Description");
        when(todoRepository.findById(id)).thenReturn(Optional.of(existingTodo));
        String tooLongDescription = "a".repeat(1001);

        // when & then
        assertThatThrownBy(() -> todoService.patchTodo(id, "New Title", tooLongDescription))
                .isInstanceOf(TodoValidationException.class)
                .hasMessageContaining("Description cannot be longer than 1000 characters");
        verify(todoRepository, never()).save(any(Todo.class));
    }
} 