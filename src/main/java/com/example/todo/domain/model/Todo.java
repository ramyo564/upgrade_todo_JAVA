package com.example.todo.domain.model;

import com.example.todo.domain.exception.TodoValidationException;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column
  private String description;

  @Column(nullable = false)
  private boolean completed;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column
  private LocalDateTime completedAt;

  @Column
  private LocalDateTime updatedAt;

  @Column
  private LocalDateTime deletedAt;

  public Todo(String title, String description) {
    validateTitle(title);
    this.title = title;
    this.description = description;
    this.completed = false;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = this.createdAt;
  }

  private void validateTitle(String title) {
    if (title == null || title.trim().isEmpty()) {
      throw new TodoValidationException("Title cannot be empty");
    }
    if (title.length() > 100) {
      throw new TodoValidationException("Title cannot be longer than 100 characters");
    }
  }

  public void complete() {
    if (isDeleted()) {
      throw new TodoValidationException("Cannot complete a deleted todo");
    }
    if (!this.completed) {
      this.completed = true;
      this.completedAt = LocalDateTime.now();
      this.updatedAt = this.completedAt;
    }
  }

  public void uncompleted() {
    if (isDeleted()) {
      throw new TodoValidationException("Cannot uncomplete a deleted todo");
    }
    if (this.completed) {
      this.completed = false;
      this.completedAt = null;
      this.updatedAt = LocalDateTime.now();
    }
  }

  public void updateTitle(String title) {
    if (isDeleted()) {
      throw new TodoValidationException("Cannot update a deleted todo");
    }
    validateTitle(title);
    this.title = title;
    this.updatedAt = LocalDateTime.now();
  }

  public void updateDescription(String description) {
    if (isDeleted()) {
      throw new TodoValidationException("Cannot update a deleted todo");
    }
    if (description != null && description.length() > 1000) {
      throw new TodoValidationException("Description cannot be longer than 1000 characters");
    }
    this.description = description;
    this.updatedAt = LocalDateTime.now();
  }

  public void delete() {
    if (this.deletedAt != null) {
      throw new TodoValidationException("Todo is already deleted");
    }
    this.deletedAt = LocalDateTime.now();
  }

  public boolean isDeleted() {
    return this.deletedAt != null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Todo that = (Todo) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
} 