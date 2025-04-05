package com.example.todo.interfaces.controller;

import com.example.todo.application.service.TodoService;
import com.example.todo.domain.exception.TodoNotFoundException;
import com.example.todo.domain.exception.TodoValidationException;
import com.example.todo.domain.model.Todo;
import com.example.todo.interfaces.dto.TodoPatchRequest;
import com.example.todo.interfaces.dto.TodoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerPatchTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    private Todo createTodoWithId(Long id, String title, String description) {
        Todo todo = new Todo(title, description);
        try {
            Field idField = Todo.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(todo, id);
            
            Field createdAtField = Todo.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(todo, LocalDateTime.now());
            
            Field updatedAtField = Todo.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(todo, LocalDateTime.now());
        } catch (Exception e) {
            throw new RuntimeException("Failed to set fields via reflection", e);
        }
        return todo;
    }

    @Test
    @DisplayName("제목만 업데이트할 수 있어야 한다")
    void patchTodoWithTitleOnly() throws Exception {
        // given
        Long id = 1L;
        TodoPatchRequest request = new TodoPatchRequest();
        request.setTitle("Updated Title");
        
        Todo updatedTodo = createTodoWithId(id, "Updated Title", "Original Description");
        
        when(todoService.patchTodo(eq(id), eq("Updated Title"), eq(null)))
                .thenReturn(updatedTodo);

        // when & then
        mockMvc.perform(patch("/api/todos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Original Description"));
    }

    @Test
    @DisplayName("설명만 업데이트할 수 있어야 한다")
    void patchTodoWithDescriptionOnly() throws Exception {
        // given
        Long id = 1L;
        TodoPatchRequest request = new TodoPatchRequest();
        request.setDescription("Updated Description");
        
        Todo updatedTodo = createTodoWithId(id, "Original Title", "Updated Description");
        
        when(todoService.patchTodo(eq(id), eq(null), eq("Updated Description")))
                .thenReturn(updatedTodo);

        // when & then
        mockMvc.perform(patch("/api/todos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Original Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    @DisplayName("제목과 설명 모두 업데이트할 수 있어야 한다")
    void patchTodoWithTitleAndDescription() throws Exception {
        // given
        Long id = 1L;
        TodoPatchRequest request = new TodoPatchRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        
        Todo updatedTodo = createTodoWithId(id, "Updated Title", "Updated Description");
        
        when(todoService.patchTodo(eq(id), eq("Updated Title"), eq("Updated Description")))
                .thenReturn(updatedTodo);

        // when & then
        mockMvc.perform(patch("/api/todos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    @DisplayName("존재하지 않는 Todo를 패치하려고 하면 404 상태 코드를 반환해야 한다")
    void patchNonExistentTodo() throws Exception {
        // given
        Long id = 1L;
        TodoPatchRequest request = new TodoPatchRequest();
        request.setTitle("Updated Title");
        
        when(todoService.patchTodo(eq(id), eq("Updated Title"), eq(null)))
                .thenThrow(new TodoNotFoundException(id));

        // when & then
        mockMvc.perform(patch("/api/todos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Todo not found with id: " + id));
    }

    @Test
    @DisplayName("유효하지 않은 입력으로 패치하려고 하면 400 상태 코드를 반환해야 한다")
    void patchWithInvalidInput() throws Exception {
        // given
        Long id = 1L;
        TodoPatchRequest request = new TodoPatchRequest();
        request.setTitle("");
        
        when(todoService.patchTodo(eq(id), eq(""), eq(null)))
                .thenThrow(new TodoValidationException("Title cannot be empty"));

        // when & then
        mockMvc.perform(patch("/api/todos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Title cannot be empty"));
    }
} 