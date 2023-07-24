package com.example.knu.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUsernameUpdateDto {



    @NotBlank(message = " 새로운 닉네임을 입력해주세요.")
    @Size(min = 2, max = 15, message = "닉네임은 2 ~ 15자 사이로 입력해주세요.")
    private String nickname;
}
