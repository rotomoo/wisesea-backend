package com.example.knu.dto.board.response;

import com.example.knu.domain.entity.board.BoardPost;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardPostUpdateResponseDto {
    private Long id;
    private String title;
    private String contents;

    public BoardPostUpdateResponseDto(BoardPost boardPost) {
        this.id = boardPost.getId();
        this.title = boardPost.getTitle();
        this.contents = boardPost.getContents();
    }
}
