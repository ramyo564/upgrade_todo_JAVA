package com.example.todo.todo.infrastructure.persistence;

import com.example.todo.todo.domain.model.Todo;
import com.example.todo.todo.domain.repository.TodoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TodoRepositoryImpl implements TodoRepository {
    
    private final JpaTodoRepository jpaTodoRepository;
    
    public TodoRepositoryImpl(JpaTodoRepository jpaTodoRepository) {
        this.jpaTodoRepository = jpaTodoRepository;
    }
    
    @Override
    public Todo save(Todo todo) {
        return jpaTodoRepository.save(todo);
    }
    
    @Override
    public Optional<Todo> findById(Long id) {
        return jpaTodoRepository.findById(id);
    }
    
    @Override
    public List<Todo> findAll() {
        return jpaTodoRepository.findAll();
    }
    
    @Override
    public void deleteById(Long id) {
        jpaTodoRepository.deleteById(id);
    }
    
    @Override
    public List<Todo> findByCompleted(boolean completed) {
        return jpaTodoRepository.findByCompleted(completed);
    }
} 