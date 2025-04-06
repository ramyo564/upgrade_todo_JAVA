package com.example.todo.interfaces.dto;

import com.example.todo.domain.model.Gender;
import com.example.todo.domain.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 사용자 응답 DTO
 */
@Getter
@Schema(description = "사용자 정보 응답")
public class UserResponse {

    @Schema(description = "사용자 ID")
    private final Long id;

    @Schema(description = "이메일 (아이디)")
    private final String email;

    @Schema(description = "닉네임")
    private final String nickname;

    @Schema(description = "나이")
    private final int age;

    @Schema(description = "성별")
    private final Gender gender;

    @Schema(description = "생성 시간")
    private final LocalDateTime createdAt;

    @Schema(description = "수정 시간")
    private final LocalDateTime updatedAt;

    @Schema(description = "삭제 시간")
    private final LocalDateTime deletedAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.deletedAt = user.getDeletedAt();
    }
} 