package com.example.todo.user.domain.repository;

import com.example.todo.user.domain.model.User;
import java.util.Optional;

/**
 * 사용자 저장소 인터페이스
 */
public interface UserRepository {
    
    /**
     * 사용자 저장
     * @param user 저장할 사용자
     * @return 저장된 사용자
     */
    User save(User user);
    
    /**
     * ID로 사용자 조회
     * @param id 사용자 ID
     * @return 조회된 사용자 (없으면 Optional.empty())
     */
    Optional<User> findById(Long id);
    
    /**
     * 이메일로 사용자 조회
     * @param email 사용자 이메일
     * @return 조회된 사용자 (없으면 Optional.empty())
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 사용자 삭제
     * @param id 삭제할 사용자의 ID
     */
    void deleteById(Long id);

    boolean existsByEmail(String email);

    Optional<User> findByNickname(String nickname);

    void delete(User user);

    boolean existsByNickname(String nickname);
} 