package com.example.todo.infrastructure.persistence;

import com.example.todo.domain.model.Todo;
import com.example.todo.domain.repository.TodoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaTodoRepository extends JpaRepository<Todo, Long>, TodoRepository {
    @Override
    List<Todo> findByCompleted(boolean completed);
} 