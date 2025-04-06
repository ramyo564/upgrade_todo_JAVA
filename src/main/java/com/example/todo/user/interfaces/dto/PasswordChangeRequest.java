package com.example.todo.user.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

/**
 * 비밀번호 변경 요청 DTO
 */
@Getter
@Builder
@Schema(description = "비밀번호 변경 요청")
public class PasswordChangeRequest {

    @Schema(description = "새 비밀번호", example = "newPassword123")
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private final String password;
} 