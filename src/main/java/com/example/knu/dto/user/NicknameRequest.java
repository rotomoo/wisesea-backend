package com.example.knu.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NicknameRequest {

    @NotBlank
    private String nickname;
}
