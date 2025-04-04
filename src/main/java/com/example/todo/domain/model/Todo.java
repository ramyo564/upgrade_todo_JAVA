package com.example.todo.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    this.title = title;
    this.description = description;
    this.completed = false;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = this.createdAt;
  }

  public void complete() {
    if (!this.completed) {
      this.completed = true;
      this.completedAt = LocalDateTime.now();
      this.updatedAt = this.completedAt;
    }
  }

  public void uncompleted() {
    if (this.completed) {
      this.completed = false;
      this.completedAt = null;
      this.updatedAt = LocalDateTime.now();
    }
  }

  public void updateTitle(String title) {
    if (title == null || title.trim().isEmpty()) {
      throw new IllegalArgumentException("Title cannot be empty");
    }
    this.title = title;
    this.updatedAt = LocalDateTime.now();
  }

  public void updateDescription(String description) {
    this.description = description;
    this.updatedAt = LocalDateTime.now();
  }

  public void delete() {
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