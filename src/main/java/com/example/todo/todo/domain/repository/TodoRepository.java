package com.example.todo.todo.domain.repository;

import com.example.todo.todo.domain.model.Todo;
import java.util.List;
import java.util.Optional;

public interface TodoRepository {

  Todo save(Todo todo);

  Optional<Todo> findById(Long id);

  List<Todo> findAll();

  void deleteById(Long id);

  List<Todo> findByCompleted(boolean completed);
} 