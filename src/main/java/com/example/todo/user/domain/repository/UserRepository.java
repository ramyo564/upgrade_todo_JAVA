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
     * 닉네임으로 사용자 조회
     * @param nickname 사용자 닉네임
     * @return 조회된 사용자 (없으면 Optional.empty())
     */
    Optional<User> findByNickname(String nickname);
    
    /**
     * 사용자 삭제
     * @param user 삭제할 사용자
     */
    void delete(User user);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
} 