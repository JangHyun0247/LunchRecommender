package com.sparta.lunchrecommender.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequestDto {
    @Pattern(regexp = "^[A-Za-z\\d]{10,20}$", message = "아이디는 최소 10자 이상 최대 20자 이하입니다.")
    private String loginId;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*?.,;:])[A-Za-z\\d!@#$%^&*?.,;:]{10,}$"
                            ,message = "비밀번호는 대문자, 소문자, 숫자, 특수 기호를 하나씩 포함해야 하며, 최소 10자 이상이어야 합니다.")
    private String password;
    @NotBlank(message = "필수 입력 값입니다.")
    private String name;
    @NotBlank(message = "필수 입력 값입니다.")
    private String nickname;
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;
    private String intro;
}
