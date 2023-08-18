package com.example.knu.dto.user;

import com.example.knu.domain.entity.enums.UserType;
import lombok.Data;

@Data
public class ProfileUpdateRequest {

    private String username;

    private UserType userType;

    private Boolean emailReceiveYn;
}
