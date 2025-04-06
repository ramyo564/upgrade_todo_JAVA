package com.example.todo.domain.model;

import com.example.todo.todo.domain.exception.TodoValidationException;
import com.example.todo.todo.domain.model.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TodoValidationTest {

    @Test
    @DisplayName("빈 제목으로 Todo를 생성하면 예외가 발생해야 한다")
    void createTodoWithEmptyTitle() {
        // when & then
        assertThrows(TodoValidationException.class, () -> {
            new Todo("", "설명");
        });
    }

    @Test
    @DisplayName("null 제목으로 Todo를 생성하면 예외가 발생해야 한다")
    void createTodoWithNullTitle() {
        // when & then
        assertThrows(TodoValidationException.class, () -> {
            new Todo(null, "설명");
        });
    }

    @Test
    @DisplayName("100자를 초과하는 제목으로 Todo를 생성하면 예외가 발생해야 한다")
    void createTodoWithTooLongTitle() {
        // given
        String longTitle = "a".repeat(101);

        // when & then
        assertThrows(TodoValidationException.class, () -> {
            new Todo(longTitle, "설명");
        });
    }

    @Test
    @DisplayName("1000자를 초과하는 설명으로 Todo를 업데이트하면 예외가 발생해야 한다")
    void updateTodoWithTooLongDescription() {
        // given
        Todo todo = new Todo("테스트", "설명");
        String longDescription = "a".repeat(1001);

        // when & then
        assertThrows(TodoValidationException.class, () -> {
            todo.updateDescription(longDescription);
        });
    }

    @Test
    @DisplayName("삭제된 Todo를 완료하려고 하면 예외가 발생해야 한다")
    void completeDeletedTodo() {
        // given
        Todo todo = new Todo("테스트", "설명");
        todo.delete();

        // when & then
        assertThrows(TodoValidationException.class, () -> {
            todo.complete();
        });
    }

    @Test
    @DisplayName("삭제된 Todo를 미완료 처리하려고 하면 예외가 발생해야 한다")
    void uncompleteDeletedTodo() {
        // given
        Todo todo = new Todo("테스트", "설명");
        todo.delete();

        // when & then
        assertThrows(TodoValidationException.class, () -> {
            todo.uncompleted();
        });
    }

    @Test
    @DisplayName("삭제된 Todo의 제목을 업데이트하려고 하면 예외가 발생해야 한다")
    void updateTitleOfDeletedTodo() {
        // given
        Todo todo = new Todo("테스트", "설명");
        todo.delete();

        // when & then
        assertThrows(TodoValidationException.class, () -> {
            todo.updateTitle("새로운 제목");
        });
    }

    @Test
    @DisplayName("삭제된 Todo의 설명을 업데이트하려고 하면 예외가 발생해야 한다")
    void updateDescriptionOfDeletedTodo() {
        // given
        Todo todo = new Todo("테스트", "설명");
        todo.delete();

        // when & then
        assertThrows(TodoValidationException.class, () -> {
            todo.updateDescription("새로운 설명");
        });
    }

    @Test
    @DisplayName("이미 삭제된 Todo를 다시 삭제하려고 하면 예외가 발생해야 한다")
    void deleteAlreadyDeletedTodo() {
        // given
        Todo todo = new Todo("테스트", "설명");
        todo.delete();

        // when & then
        assertThrows(TodoValidationException.class, () -> {
            todo.delete();
        });
    }
} 