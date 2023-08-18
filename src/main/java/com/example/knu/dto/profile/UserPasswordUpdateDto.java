package com.example.knu.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordUpdateDto {

    @NotBlank(message = "기존 비밀번호를 입력해주세요.")
    private String currentPassword;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    private String newPassword;

    @NotBlank(message = "다시 한번 입력해주세요.")
    private String confirmPassword;
}
