package com.example.knu.dto.board.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardPostUpdateRequestDto {
    private String title;
    private String contents;
}
