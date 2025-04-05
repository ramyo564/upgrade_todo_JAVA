package com.example.todo.domain.model;

import com.example.todo.domain.exception.TodoValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TodoTest {

    @Test
    @DisplayName("Todo 생성 시 기본값이 올바르게 설정되어야 한다")
    void createTodo() {
        // given
        String title = "테스트 할일";
        String description = "테스트 설명";

        // when
        Todo todo = new Todo(title, description);

        // then
        assertThat(todo.getTitle()).isEqualTo(title);
        assertThat(todo.getDescription()).isEqualTo(description);
        assertThat(todo.isCompleted()).isFalse();
        assertThat(todo.getCreatedAt()).isNotNull();
        assertThat(todo.getUpdatedAt()).isEqualTo(todo.getCreatedAt());
        assertThat(todo.getCompletedAt()).isNull();
        assertThat(todo.getDeletedAt()).isNull();
    }

    @Test
    @DisplayName("Todo 완료 시 상태가 올바르게 변경되어야 한다")
    void completeTodo() {
        // given
        Todo todo = new Todo("테스트", "설명");

        // when
        todo.complete();

        // then
        assertThat(todo.isCompleted()).isTrue();
        assertThat(todo.getCompletedAt()).isNotNull();
        assertThat(todo.getUpdatedAt()).isEqualTo(todo.getCompletedAt());
    }

    @Test
    @DisplayName("이미 완료된 Todo를 다시 완료하려고 하면 상태가 변경되지 않아야 한다")
    void completeAlreadyCompletedTodo() {
        // given
        Todo todo = new Todo("테스트", "설명");
        todo.complete();
        var completedAt = todo.getCompletedAt();
        var updatedAt = todo.getUpdatedAt();

        // when
        todo.complete();

        // then
        assertThat(todo.isCompleted()).isTrue();
        assertThat(todo.getCompletedAt()).isEqualTo(completedAt);
        assertThat(todo.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Todo 미완료 시 상태가 올바르게 변경되어야 한다")
    void uncompleteTodo() {
        // given
        Todo todo = new Todo("테스트", "설명");
        todo.complete();

        // when
        todo.uncompleted();

        // then
        assertThat(todo.isCompleted()).isFalse();
        assertThat(todo.getCompletedAt()).isNull();
        assertThat(todo.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("제목 업데이트 시 올바르게 변경되어야 한다")
    void updateTitle() {
        // given
        Todo todo = new Todo("테스트", "설명");
        String newTitle = "새로운 제목";

        // when
        todo.updateTitle(newTitle);

        // then
        assertThat(todo.getTitle()).isEqualTo(newTitle);
        assertThat(todo.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("빈 제목으로 업데이트 시 예외가 발생해야 한다")
    void updateTitleWithEmptyTitle() {
        // given
        Todo todo = new Todo("테스트", "설명");

        // when & then
        assertThrows(TodoValidationException.class, () -> {
            todo.updateTitle("");
        });
    }

    @Test
    @DisplayName("설명 업데이트 시 올바르게 변경되어야 한다")
    void updateDescription() {
        // given
        Todo todo = new Todo("테스트", "설명");
        String newDescription = "새로운 설명";

        // when
        todo.updateDescription(newDescription);

        // then
        assertThat(todo.getDescription()).isEqualTo(newDescription);
        assertThat(todo.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Todo 삭제 시 삭제 시간이 설정되어야 한다")
    void deleteTodo() {
        // given
        Todo todo = new Todo("테스트", "설명");

        // when
        todo.delete();

        // then
        assertThat(todo.getDeletedAt()).isNotNull();
        assertThat(todo.isDeleted()).isTrue();
    }
} 