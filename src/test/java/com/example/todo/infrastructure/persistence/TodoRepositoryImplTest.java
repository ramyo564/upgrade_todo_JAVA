package com.example.todo.infrastructure.persistence;

import com.example.todo.todo.domain.model.Todo;
import com.example.todo.todo.infrastructure.persistence.JpaTodoRepository;
import com.example.todo.todo.infrastructure.persistence.TodoRepositoryImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoRepositoryImplTest {

    @Mock
    private JpaTodoRepository jpaTodoRepository;

    @InjectMocks
    private TodoRepositoryImpl todoRepository;

    @Test
    @DisplayName("Todo를 저장할 수 있어야 한다")
    void save() {
        // given
        Todo todo = new Todo("테스트", "설명");
        when(jpaTodoRepository.save(any(Todo.class))).thenReturn(todo);

        // when
        Todo savedTodo = todoRepository.save(todo);

        // then
        assertThat(savedTodo).isNotNull();
        assertThat(savedTodo.getTitle()).isEqualTo("테스트");
        verify(jpaTodoRepository).save(todo);
    }

    @Test
    @DisplayName("ID로 Todo를 조회할 수 있어야 한다")
    void findById() {
        // given
        Long id = 1L;
        Todo todo = new Todo("테스트", "설명");
        when(jpaTodoRepository.findById(id)).thenReturn(Optional.of(todo));

        // when
        Optional<Todo> foundTodo = todoRepository.findById(id);

        // then
        assertThat(foundTodo).isPresent();
        assertThat(foundTodo.get().getTitle()).isEqualTo("테스트");
    }

    @Test
    @DisplayName("모든 Todo를 조회할 수 있어야 한다")
    void findAll() {
        // given
        Todo todo1 = new Todo("테스트1", "설명1");
        Todo todo2 = new Todo("테스트2", "설명2");
        when(jpaTodoRepository.findAll()).thenReturn(Arrays.asList(todo1, todo2));

        // when
        List<Todo> todos = todoRepository.findAll();

        // then
        assertThat(todos).hasSize(2);
        assertThat(todos).extracting("title").containsExactly("테스트1", "테스트2");
    }

    @Test
    @DisplayName("ID로 Todo를 삭제할 수 있어야 한다")
    void deleteById() {
        // given
        Long id = 1L;

        // when
        todoRepository.deleteById(id);

        // then
        verify(jpaTodoRepository).deleteById(id);
    }

    @Test
    @DisplayName("완료 상태로 Todo를 조회할 수 있어야 한다")
    void findByCompleted() {
        // given
        Todo todo1 = new Todo("테스트1", "설명1");
        Todo todo2 = new Todo("테스트2", "설명2");
        todo1.complete();
        when(jpaTodoRepository.findByCompleted(true)).thenReturn(Arrays.asList(todo1));

        // when
        List<Todo> completedTodos = todoRepository.findByCompleted(true);

        // then
        assertThat(completedTodos).hasSize(1);
        assertThat(completedTodos.get(0).getTitle()).isEqualTo("테스트1");
    }
} 