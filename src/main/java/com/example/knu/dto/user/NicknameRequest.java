package com.example.knu.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NicknameRequest {

    @NotNull
    private String nickname;
}
