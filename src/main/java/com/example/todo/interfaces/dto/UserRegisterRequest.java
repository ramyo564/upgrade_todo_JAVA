package com.example.todo.interfaces.dto;

import com.example.todo.domain.model.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 등록 요청 DTO
 */
@Getter
@Builder
@Schema(description = "사용자 등록 요청")
public class UserRegisterRequest {

    @Schema(description = "이메일 (아이디)", example = "user@example.com")
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private final String email;

    @Schema(description = "비밀번호", example = "password123")
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private final String password;

    @Schema(description = "닉네임", example = "사용자1")
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다.")
    private final String nickname;

    @Schema(description = "나이", example = "25")
    @NotNull(message = "나이는 필수입니다.")
    @Min(value = 1, message = "나이는 1세 이상이어야 합니다.")
    @Max(value = 100, message = "나이는 100세 이하여야 합니다.")
    private final Integer age;

    @Schema(description = "성별", example = "MALE")
    @NotNull(message = "성별은 필수입니다.")
    private final Gender gender;
} 