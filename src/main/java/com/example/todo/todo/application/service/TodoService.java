package com.example.todo.todo.application.service;

import com.example.todo.todo.domain.exception.TodoNotFoundException;
import com.example.todo.todo.domain.model.Todo;
import com.example.todo.todo.domain.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TodoService {
    private static final Logger log = LoggerFactory.getLogger(TodoService.class);
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo createTodo(String title, String description) {
        log.debug("Creating new Todo with title: {}", title);
        Todo todo = new Todo(title, description);
        Todo savedTodo = todoRepository.save(todo);
        log.info("Created new Todo with ID: {}", savedTodo.getId());
        return savedTodo;
    }

    @Transactional(readOnly = true)
    public Todo getTodo(Long id) {
        log.debug("Fetching Todo with ID: {}", id);
        return todoRepository.findById(id)
                .filter(todo -> !todo.isDeleted())
                .orElseThrow(() -> {
                    log.warn("Todo not found with ID: {}", id);
                    return new TodoNotFoundException(id);
                });
    }

    @Transactional(readOnly = true)
    public List<Todo> getAllTodos() {
        log.debug("Fetching all non-deleted Todos");
        return todoRepository.findAll().stream()
                .filter(todo -> !todo.isDeleted())
                .collect(Collectors.toList());
    }

    public List<Todo> getCompletedTodos() {
        return todoRepository.findByCompleted(true).stream()
                .filter(todo -> !todo.isDeleted())
                .collect(Collectors.toList());
    }

    public List<Todo> getIncompleteTodos() {
        return todoRepository.findByCompleted(false).stream()
                .filter(todo -> !todo.isDeleted())
                .collect(Collectors.toList());
    }

    public Todo updateTodo(Long id, String title, String description) {
        log.debug("Updating Todo with ID: {} with title: {} and description: {}", id, title, description);
        Todo todo = getTodo(id);
        todo.updateTitle(title);
        todo.updateDescription(description);
        Todo updatedTodo = todoRepository.save(todo);
        log.info("Updated Todo with ID: {}", id);
        return updatedTodo;
    }

    public Todo patchTodo(Long id, String title, String description) {
        log.debug("Patching Todo with ID: {}", id);
        Todo todo = getTodo(id);
        
        if (title != null) {
            log.debug("Updating title for Todo with ID: {}", id);
            todo.updateTitle(title);
        }
        
        if (description != null) {
            log.debug("Updating description for Todo with ID: {}", id);
            todo.updateDescription(description);
        }
        
        Todo patchedTodo = todoRepository.save(todo);
        log.info("Patched Todo with ID: {}", id);
        return patchedTodo;
    }

    public Todo completeTodo(Long id) {
        log.debug("Completing Todo with ID: {}", id);
        Todo todo = getTodo(id);
        todo.complete();
        Todo completedTodo = todoRepository.save(todo);
        log.info("Completed Todo with ID: {}", id);
        return completedTodo;
    }

    public Todo uncompleteTodo(Long id) {
        log.debug("Uncompleting Todo with ID: {}", id);
        Todo todo = getTodo(id);
        todo.uncompleted();
        Todo uncompletedTodo = todoRepository.save(todo);
        log.info("Uncompleted Todo with ID: {}", id);
        return uncompletedTodo;
    }

    public void deleteTodo(Long id) {
        log.debug("Deleting Todo with ID: {}", id);
        Todo todo = getTodo(id);
        todo.delete();
        todoRepository.save(todo);
        log.info("Deleted Todo with ID: {}", id);
    }
} 