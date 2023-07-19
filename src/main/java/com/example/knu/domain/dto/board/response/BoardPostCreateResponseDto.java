package com.example.knu.domain.dto.board.response;

import com.example.knu.domain.entity.board.BoardPost;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardPostCreateResponseDto {
    private String title;
    private String contents;

    public BoardPostCreateResponseDto(BoardPost boardPost) {
        this.title = boardPost.getTitle();
        this.contents = boardPost.getContents();
    }
}
