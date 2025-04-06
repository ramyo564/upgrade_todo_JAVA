package com.example.todo.todo.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request object for creating or updating a todo")
public class TodoRequest {
    @Schema(description = "Title of the todo", example = "Complete project documentation")
    @NotBlank(message = "Title is required")
    private String title;
    
    @Schema(description = "Description of the todo", example = "Write comprehensive documentation for the project")
    private String description;
} 