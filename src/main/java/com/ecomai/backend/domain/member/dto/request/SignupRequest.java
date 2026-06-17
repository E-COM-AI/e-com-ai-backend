package com.ecomai.backend.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청 DTO
 */
@Getter
@NoArgsConstructor
@Schema(description = "회원가입 요청")
public class SignupRequest {

    /**
     * 이메일
     */
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    @Schema(
            description = "회원 이메일",
            example = "user@test.com"
    )
    private String email;

    /**
     * 비밀번호
     */
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")
    @Schema(
            description = "회원 비밀번호",
            example = "password1234"
    )
    private String password;
}