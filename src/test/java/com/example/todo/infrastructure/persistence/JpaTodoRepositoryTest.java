package com.example.todo.infrastructure.persistence;

import com.example.todo.domain.model.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class JpaTodoRepositoryTest {

    @Autowired
    private JpaTodoRepository jpaTodoRepository;

    @Test
    @DisplayName("Todo를 저장하고 조회할 수 있어야 한다")
    void saveAndFind() {
        // given
        Todo todo = new Todo("테스트", "설명");

        // when
        Todo savedTodo = jpaTodoRepository.save(todo);

        // then
        assertThat(savedTodo.getId()).isNotNull();
        assertThat(savedTodo.getTitle()).isEqualTo("테스트");
        assertThat(savedTodo.getDescription()).isEqualTo("설명");
    }

    @Test
    @DisplayName("완료된 Todo만 조회할 수 있어야 한다")
    void findByCompleted() {
        // given
        Todo todo1 = new Todo("테스트1", "설명1");
        Todo todo2 = new Todo("테스트2", "설명2");
        todo1.complete();
        jpaTodoRepository.save(todo1);
        jpaTodoRepository.save(todo2);

        // when
        List<Todo> completedTodos = jpaTodoRepository.findByCompleted(true);

        // then
        assertThat(completedTodos).hasSize(1);
        assertThat(completedTodos.get(0).getTitle()).isEqualTo("테스트1");
    }

    @Test
    @DisplayName("미완료된 Todo만 조회할 수 있어야 한다")
    void findByNotCompleted() {
        // given
        Todo todo1 = new Todo("테스트1", "설명1");
        Todo todo2 = new Todo("테스트2", "설명2");
        todo1.complete();
        jpaTodoRepository.save(todo1);
        jpaTodoRepository.save(todo2);

        // when
        List<Todo> incompleteTodos = jpaTodoRepository.findByCompleted(false);

        // then
        assertThat(incompleteTodos).hasSize(1);
        assertThat(incompleteTodos.get(0).getTitle()).isEqualTo("테스트2");
    }

    @Test
    @DisplayName("Todo를 업데이트할 수 있어야 한다")
    void updateTodo() {
        // given
        Todo todo = new Todo("테스트", "설명");
        Todo savedTodo = jpaTodoRepository.save(todo);

        // when
        savedTodo.updateTitle("새로운 제목");
        savedTodo.updateDescription("새로운 설명");
        Todo updatedTodo = jpaTodoRepository.save(savedTodo);

        // then
        assertThat(updatedTodo.getTitle()).isEqualTo("새로운 제목");
        assertThat(updatedTodo.getDescription()).isEqualTo("새로운 설명");
    }

    @Test
    @DisplayName("Todo를 삭제할 수 있어야 한다")
    void deleteTodo() {
        // given
        Todo todo = new Todo("테스트", "설명");
        Todo savedTodo = jpaTodoRepository.save(todo);

        // when
        savedTodo.delete();
        jpaTodoRepository.save(savedTodo);

        // then
        assertThat(savedTodo.isDeleted()).isTrue();
    }
} 