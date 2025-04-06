package com.example.todo.todo.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request object for partially updating a todo")
public class TodoPatchRequest {
    @Schema(description = "Title of the todo", example = "Complete project documentation")
    private String title;
    
    @Schema(description = "Description of the todo", example = "Write comprehensive documentation for the project")
    private String description;
} 