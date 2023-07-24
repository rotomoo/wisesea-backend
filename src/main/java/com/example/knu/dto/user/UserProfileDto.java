package com.example.knu.dto.user;

import com.example.knu.domain.entity.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserProfileDto {
    private Long userId;
    private String loginId;
    private String password;
    private String username;
    private UserType userType;
    private Boolean emailReceiveYn;
    private String nickname;
    private String profileImageUrl;
    private boolean activated;
}
