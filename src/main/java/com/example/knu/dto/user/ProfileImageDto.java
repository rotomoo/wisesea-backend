package com.example.knu.dto.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileImageDto {

    private MultipartFile profileImage;
}
