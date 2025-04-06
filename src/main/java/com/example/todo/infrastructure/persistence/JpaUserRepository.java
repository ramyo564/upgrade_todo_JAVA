package com.example.todo.infrastructure.persistence;

import com.example.todo.domain.model.User;
import com.example.todo.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA 사용자 저장소 구현체
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
    
    @Override
    Optional<User> findByEmail(String email);
    
    @Override
    Optional<User> findByNickname(String nickname);
} 