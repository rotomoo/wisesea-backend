package com.example.knu.dto.notice;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class NoticeUpdate {

    @NotBlank
    private String title;
    @NotBlank
    private String contents;
    private List<MultipartFile> files;
    private List<String> hashtags;
}
