package com.example.todo.todo.interfaces.dto;

import com.example.todo.todo.domain.model.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "Response object for todo operations")
public class TodoResponse {
    @Schema(description = "Unique identifier of the todo")
    private final Long id;
    
    @Schema(description = "Title of the todo")
    private final String title;
    
    @Schema(description = "Description of the todo")
    private final String description;
    
    @Schema(description = "Completion status of the todo")
    private final boolean completed;
    
    @Schema(description = "Creation timestamp of the todo")
    private final LocalDateTime createdAt;
    
    @Schema(description = "Completion timestamp of the todo")
    private final LocalDateTime completedAt;

    @Schema(description = "Last update timestamp of the todo")
    private final LocalDateTime updatedAt;

    @Schema(description = "Deletion timestamp of the todo")
    private final LocalDateTime deletedAt;

    public TodoResponse(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.description = todo.getDescription();
        this.completed = todo.isCompleted();
        this.createdAt = todo.getCreatedAt();
        this.completedAt = todo.getCompletedAt();
        this.updatedAt = todo.getUpdatedAt();
        this.deletedAt = todo.getDeletedAt();
    }
} 