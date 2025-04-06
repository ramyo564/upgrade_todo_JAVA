package com.example.todo.interfaces.controller;

import com.example.todo.application.service.UserService;
import com.example.todo.domain.model.Gender;
import com.example.todo.domain.model.User;
import com.example.todo.interfaces.dto.PasswordChangeRequest;
import com.example.todo.interfaces.dto.UserRegisterRequest;
import com.example.todo.interfaces.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 컨트롤러
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 사용자 등록
     */
    @Operation(
        summary = "사용자 등록",
        description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자 등록 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<UserResponse> registerUser(
        @Valid @RequestBody UserRegisterRequest request) {
        User user = userService.registerUser(
            request.getEmail(),
            request.getPassword(),
            request.getNickname(),
            request.getAge(),
            request.getGender()
        );
        return ResponseEntity.ok(new UserResponse(user));
    }

    /**
     * 사용자 정보 조회
     */
    @Operation(
        summary = "사용자 정보 조회",
        description = "이메일로 사용자 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUser(
        @Parameter(description = "사용자 이메일")
        @PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(new UserResponse(user));
    }

    /**
     * 사용자 정보 수정
     */
    @Operation(
        summary = "사용자 정보 수정",
        description = "사용자 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/{email}")
    public ResponseEntity<UserResponse> updateUser(
        @Parameter(description = "사용자 이메일")
        @PathVariable String email,
        @Valid @RequestBody UserRegisterRequest request) {
        User user = userService.updateUser(
            email,
            request.getNickname(),
            request.getAge(),
            request.getGender()
        );
        return ResponseEntity.ok(new UserResponse(user));
    }

    /**
     * 비밀번호 변경
     */
    @Operation(
        summary = "비밀번호 변경",
        description = "사용자의 비밀번호를 변경합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/{email}/password")
    public ResponseEntity<UserResponse> changePassword(
        @Parameter(description = "사용자 이메일")
        @PathVariable String email,
        @Valid @RequestBody PasswordChangeRequest request) {
        User user = userService.changePassword(email, request.getPassword());
        return ResponseEntity.ok(new UserResponse(user));
    }

    /**
     * 사용자 삭제
     */
    @Operation(
        summary = "사용자 삭제",
        description = "사용자를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "사용자 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "사용자 이메일")
        @PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
} 