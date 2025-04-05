package com.example.todo.application.service;

import com.example.todo.domain.exception.TodoNotFoundException;
import com.example.todo.domain.model.Todo;
import com.example.todo.domain.repository.TodoRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

  private final TodoRepository todoRepository;

  @Transactional
  public Todo createTodo(String title, String description) {
    Todo todo = new Todo(title, description);
    return todoRepository.save(todo);
  }

  public Todo getTodo(Long id) {
    return todoRepository.findById(id)
        .filter(todo -> !todo.isDeleted())
        .orElseThrow(() -> new TodoNotFoundException(id));
  }

  public List<Todo> getAllTodos() {
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

  @Transactional
  public Todo updateTodo(Long id, String title, String description) {
    Todo todo = getTodo(id);
    todo.updateTitle(title);
    todo.updateDescription(description);
    return todoRepository.save(todo);
  }

  @Transactional
  public Todo completeTodo(Long id) {
    Todo todo = getTodo(id);
    todo.complete();
    return todoRepository.save(todo);
  }

  @Transactional
  public Todo uncompleteTodo(Long id) {
    Todo todo = getTodo(id);
    todo.uncompleted();
    return todoRepository.save(todo);
  }

  @Transactional
  public void deleteTodo(Long id) {
    Todo todo = getTodo(id);
    todo.delete();
    todoRepository.save(todo);
  }
} 