package com.example.todo.interfaces.controller;

import com.example.todo.application.service.TodoService;
import com.example.todo.interfaces.dto.TodoRequest;
import com.example.todo.interfaces.dto.TodoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Tag(name = "Todo Controller", description = "Todo management APIs")
public class TodoController {

  private final TodoService todoService;

  @Operation(
      summary = "Create a new todo",
      description = "Creates a new todo item with the provided title and description")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Todo created successfully",
          content = @Content(schema = @Schema(implementation = TodoResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input")
  })
  @PostMapping
  public ResponseEntity<TodoResponse> createTodo(
      @Valid @RequestBody TodoRequest request) {
    return ResponseEntity.ok(new TodoResponse(
        todoService.createTodo(request.getTitle(),
            request.getDescription())
    ));
  }

  @Operation(
      summary = "Get a todo by ID",
      description = "Retrieves a todo item by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Todo found",
          content = @Content(schema = @Schema(implementation = TodoResponse.class))),
      @ApiResponse(responseCode = "404", description = "Todo not found")
  })
  @GetMapping("/{id}")
  public ResponseEntity<TodoResponse> getTodo(
      @Parameter(description = "ID of the todo to retrieve")
      @PathVariable Long id) {
    return ResponseEntity.ok(new TodoResponse(todoService.getTodo(id)));
  }

  @Operation(
      summary = "Get all todos",
      description = "Retrieves all todo items")
  @ApiResponse(responseCode = "200", description = "List of all todos",
      content = @Content(schema = @Schema(implementation = TodoResponse.class)))
  @GetMapping
  public ResponseEntity<List<TodoResponse>> getAllTodos() {
    List<TodoResponse> todos = todoService.getAllTodos().stream()
        .map(TodoResponse::new)
        .collect(Collectors.toList());
    return ResponseEntity.ok(todos);
  }

  @Operation(
      summary = "Get completed todos",
      description = "Retrieves all completed todo items")
  @ApiResponse(responseCode = "200", description = "List of completed todos",
      content = @Content(schema = @Schema(implementation = TodoResponse.class)))
  @GetMapping("/completed")
  public ResponseEntity<List<TodoResponse>> getCompletedTodos() {
    List<TodoResponse> todos = todoService.getCompletedTodos().stream()
        .map(TodoResponse::new)
        .collect(Collectors.toList());
    return ResponseEntity.ok(todos);
  }

  @Operation(
      summary = "Get uncompleted todos",
      description = "Retrieves all incomplete todo items")
  @ApiResponse(responseCode = "200", description = "List of uncompleted todos",
      content = @Content(schema = @Schema(implementation = TodoResponse.class)))
  @GetMapping("/uncompleted")
  public ResponseEntity<List<TodoResponse>> getIncompleteTodos() {
    List<TodoResponse> todos = todoService.getIncompleteTodos().stream()
        .map(TodoResponse::new)
        .collect(Collectors.toList());
    return ResponseEntity.ok(todos);
  }

  @Operation(
      summary = "Update a todo",
      description = "Updates an existing todo item with new title and description")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Todo updated successfully",
          content = @Content(schema = @Schema(implementation = TodoResponse.class))),
      @ApiResponse(responseCode = "404", description = "Todo not found"),
      @ApiResponse(responseCode = "400", description = "Invalid input")
  })
  @PutMapping("/{id}")
  public ResponseEntity<TodoResponse> updateTodo(
      @Parameter(description = "ID of the todo to update")
      @PathVariable Long id,
      @Valid @RequestBody TodoRequest request) {
    return ResponseEntity.ok(new TodoResponse(
        todoService.updateTodo(id, request.getTitle(),
            request.getDescription())
    ));
  }

  @Operation(
      summary = "Complete a todo",
      description = "Marks a todo item as completed")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Todo marked as completed",
          content = @Content(schema = @Schema(implementation = TodoResponse.class))),
      @ApiResponse(responseCode = "404", description = "Todo not found")
  })
  @PutMapping("/{id}/complete")
  public ResponseEntity<TodoResponse> completeTodo(
      @Parameter(description = "ID of the todo to complete")
      @PathVariable Long id) {
    return ResponseEntity.ok(
        new TodoResponse(todoService.completeTodo(id)));
  }

  @Operation(
      summary = "Uncomplete a todo",
      description = "Marks a todo item as incomplete")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Todo marked as incomplete",
          content = @Content(schema = @Schema(implementation = TodoResponse.class))),
      @ApiResponse(responseCode = "404", description = "Todo not found")
  })
  @PutMapping("/{id}/uncomplete")
  public ResponseEntity<TodoResponse> uncompleteTodo(
      @Parameter(description = "ID of the todo to uncomplete")
      @PathVariable Long id) {
    return ResponseEntity.ok(
        new TodoResponse(todoService.uncompleteTodo(id)));
  }

  @Operation(
      summary = "Delete a todo",
      description = "Deletes a todo item by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Todo deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Todo not found")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTodo(
      @Parameter(description = "ID of the todo to delete")
      @PathVariable Long id) {
    todoService.deleteTodo(id);
    return ResponseEntity.ok().build();
  }
} 