package com.example.todo.user.infrastructure.persistence;

import com.example.todo.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA 사용자 저장소 구현체
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
} 