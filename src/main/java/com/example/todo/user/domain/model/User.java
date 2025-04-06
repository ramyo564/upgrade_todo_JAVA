package com.example.todo.user.domain.model;

import com.example.todo.user.domain.exception.UserValidationException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 사용자 도메인 모델
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    /**
     * 사용자 생성
     * @param email 이메일 (아이디)
     * @param password 비밀번호
     * @param nickname 닉네임
     * @param age 나이
     * @param gender 성별
     */
    public User(String email, String password, String nickname, int age, Gender gender) {
        validateEmail(email);
        validatePassword(password);
        validateNickname(nickname);
        validateAge(age);
        validateGender(gender);
        
        this.email = email;
        this.password = password; // 실제로는 암호화해야 함
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    /**
     * 이메일 유효성 검사
     */
    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new UserValidationException("이메일은 필수입니다.");
        }
        
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new UserValidationException("유효하지 않은 이메일 형식입니다.");
        }
    }

    /**
     * 비밀번호 유효성 검사
     */
    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new UserValidationException("비밀번호는 필수입니다.");
        }
        
        if (password.length() < 8) {
            throw new UserValidationException("비밀번호는 최소 8자 이상이어야 합니다.");
        }
    }

    /**
     * 닉네임 유효성 검사
     */
    private void validateNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new UserValidationException("닉네임은 필수입니다.");
        }
        
        if (nickname.length() < 2 || nickname.length() > 20) {
            throw new UserValidationException("닉네임은 2자 이상 20자 이하여야 합니다.");
        }
    }

    /**
     * 나이 유효성 검사
     */
    private void validateAge(int age) {
        if (age < 1 || age > 100) {
            throw new UserValidationException("나이는 1세부터 100세까지 가능합니다.");
        }
    }

    private void validateGender(Gender gender) {
        if (gender == null) {
            throw new UserValidationException("성별은 필수입니다.");
        }
    }

    /**
     * 사용자 정보 업데이트
     */
    public void update(String nickname, int age, Gender gender) {
        if (isDeleted()) {
            throw new UserValidationException("삭제된 사용자는 수정할 수 없습니다.");
        }
        
        validateNickname(nickname);
        validateAge(age);
        validateGender(gender);
        
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 비밀번호 변경
     */
    public void changePassword(String newPassword) {
        if (isDeleted()) {
            throw new UserValidationException("삭제된 사용자는 비밀번호를 변경할 수 없습니다.");
        }
        
        validatePassword(newPassword);
        this.password = newPassword; // 실제로는 암호화해야 함
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 사용자 삭제
     */
    public void delete() {
        if (this.deletedAt != null) {
            throw new UserValidationException("이미 삭제된 사용자입니다.");
        }
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * 삭제 여부 확인
     */
    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 