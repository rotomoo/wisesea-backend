package com.example.knu.dto.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull( message = "아이디를 입력해주세요")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요")

    @Size(min = 3, max = 100)
    private String password;






}