package com.example.knu.dto.board.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class BoardPostUpdateRequestDto {
    private String title;
    private String contents;
    private Long categoryId;
    private List<MultipartFile> files;
}
