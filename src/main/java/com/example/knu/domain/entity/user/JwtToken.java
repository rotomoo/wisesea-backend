package com.example.knu.domain.entity.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {

    private String grantType;
    private String accessToken;
    private String refreshToken;

}
