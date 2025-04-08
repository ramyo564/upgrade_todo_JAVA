package com.example.todo.user.application.service;

import com.example.todo.user.domain.exception.UserValidationException;
import com.example.todo.user.domain.model.Gender;
import com.example.todo.user.domain.model.User;
import com.example.todo.user.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 서비스
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 사용자 등록
     * @param email 이메일 (아이디)
     * @param password 비밀번호
     * @param nickname 닉네임
     * @param age 나이
     * @param gender 성별
     * @return 등록된 사용자
     */
    @Transactional
    public User registerUser(String email, String password, String nickname, int age, Gender gender) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(email)) {
            throw new UserValidationException("이미 사용 중인 이메일입니다.");
        }
        
        // 닉네임 중복 체크
        if (userRepository.existsByNickname(nickname)) {
            throw new UserValidationException("이미 사용 중인 닉네임입니다.");
        }
        
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);
        
        // 사용자 생성 및 저장
        User user = new User(email, encodedPassword, nickname, age, gender);
        return userRepository.save(user);
    }

    /**
     * 이메일로 사용자 조회
     * @param email 이메일
     * @return 조회된 사용자
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserValidationException("사용자를 찾을 수 없습니다."));
    }

    /**
     * 사용자 정보 수정
     * @param email 이메일
     * @param nickname 닉네임
     * @param age 나이
     * @param gender 성별
     * @return 수정된 사용자
     */
    @Transactional
    public User updateUser(String email, String nickname, int age, Gender gender) {
        User user = getUserByEmail(email);
        
        // 닉네임이 변경되는 경우 중복 체크
        if (!user.getNickname().equals(nickname) && userRepository.existsByNickname(nickname)) {
            throw new UserValidationException("이미 사용 중인 닉네임입니다.");
        }
        
        user.update(nickname, age, gender);
        return userRepository.save(user);
    }

    /**
     * 비밀번호 변경
     * @param email 이메일
     * @param newPassword 새 비밀번호
     * @return 비밀번호가 변경된 사용자
     */
    @Transactional
    public User changePassword(String email, String newPassword) {
        User user = getUserByEmail(email);
        // 새 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.changePassword(encodedPassword);
        return userRepository.save(user);
    }

    /**
     * 사용자 삭제
     * @param email 이메일
     */
    @Transactional
    public void deleteUser(String email) {
        User user = getUserByEmail(email);
        userRepository.delete(user);
    }
} 