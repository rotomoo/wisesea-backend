package com.example.knu.dto.profile;


import com.example.knu.domain.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {


    private String username;
    private String nickname;

    @Builder
    public UserResponseDto(User user) {

        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}
