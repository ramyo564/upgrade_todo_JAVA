package com.example.todo.application.service;

import com.example.todo.domain.exception.TodoNotFoundException;
import com.example.todo.domain.model.Todo;
import com.example.todo.domain.repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    @DisplayName("Todo 생성 시 저장소에 저장되어야 한다")
    void createTodo() {
        // given
        String title = "테스트 할일";
        String description = "테스트 설명";
        Todo todo = new Todo(title, description);
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // when
        Todo savedTodo = todoService.createTodo(title, description);

        // then
        assertThat(savedTodo.getTitle()).isEqualTo(title);
        assertThat(savedTodo.getDescription()).isEqualTo(description);
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    @DisplayName("ID로 Todo를 조회할 수 있어야 한다")
    void getTodo() {
        // given
        Long id = 1L;
        Todo todo = new Todo("테스트", "설명");
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

        // when
        Todo foundTodo = todoService.getTodo(id);

        // then
        assertThat(foundTodo).isNotNull();
        assertThat(foundTodo.getTitle()).isEqualTo("테스트");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 Todo를 조회하면 예외가 발생해야 한다")
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
    @DisplayName("모든 Todo를 조회할 수 있어야 한다")
    void getAllTodos() {
        // given
        Todo todo1 = new Todo("테스트1", "설명1");
        Todo todo2 = new Todo("테스트2", "설명2");
        when(todoRepository.findAll()).thenReturn(Arrays.asList(todo1, todo2));

        // when
        List<Todo> todos = todoService.getAllTodos();

        // then
        assertThat(todos).hasSize(2);
        assertThat(todos).extracting("title").containsExactly("테스트1", "테스트2");
    }

    @Test
    @DisplayName("완료된 Todo만 조회할 수 있어야 한다")
    void getCompletedTodos() {
        // given
        Todo todo1 = new Todo("테스트1", "설명1");
        Todo todo2 = new Todo("테스트2", "설명2");
        todo1.complete();
        when(todoRepository.findByCompleted(true)).thenReturn(Arrays.asList(todo1));

        // when
        List<Todo> completedTodos = todoService.getCompletedTodos();

        // then
        assertThat(completedTodos).hasSize(1);
        assertThat(completedTodos.get(0).getTitle()).isEqualTo("테스트1");
    }

    @Test
    @DisplayName("미완료된 Todo만 조회할 수 있어야 한다")
    void getIncompleteTodos() {
        // given
        Todo todo1 = new Todo("테스트1", "설명1");
        Todo todo2 = new Todo("테스트2", "설명2");
        todo1.complete();
        when(todoRepository.findByCompleted(false)).thenReturn(Arrays.asList(todo2));

        // when
        List<Todo> incompleteTodos = todoService.getIncompleteTodos();

        // then
        assertThat(incompleteTodos).hasSize(1);
        assertThat(incompleteTodos.get(0).getTitle()).isEqualTo("테스트2");
    }

    @Test
    @DisplayName("Todo를 업데이트할 수 있어야 한다")
    void updateTodo() {
        // given
        Long id = 1L;
        Todo todo = new Todo("테스트", "설명");
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // when
        Todo updatedTodo = todoService.updateTodo(id, "새로운 제목", "새로운 설명");

        // then
        assertThat(updatedTodo.getTitle()).isEqualTo("새로운 제목");
        assertThat(updatedTodo.getDescription()).isEqualTo("새로운 설명");
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    @DisplayName("Todo를 완료 처리할 수 있어야 한다")
    void completeTodo() {
        // given
        Long id = 1L;
        Todo todo = new Todo("테스트", "설명");
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // when
        Todo completedTodo = todoService.completeTodo(id);

        // then
        assertThat(completedTodo.isCompleted()).isTrue();
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    @DisplayName("Todo를 미완료 처리할 수 있어야 한다")
    void uncompleteTodo() {
        // given
        Long id = 1L;
        Todo todo = new Todo("테스트", "설명");
        todo.complete();
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // when
        Todo uncompletedTodo = todoService.uncompleteTodo(id);

        // then
        assertThat(uncompletedTodo.isCompleted()).isFalse();
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    @DisplayName("Todo를 삭제할 수 있어야 한다")
    void deleteTodo() {
        // given
        Long id = 1L;
        Todo todo = new Todo("테스트", "설명");
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // when
        todoService.deleteTodo(id);

        // then
        verify(todoRepository).save(any(Todo.class));
    }
} 