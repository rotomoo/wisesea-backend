package com.example.knu.dto.board.response;

import com.example.knu.domain.entity.board.BoardPost;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardPostOneResponseDto {
    private Long id;
//    private String nickname;
    private String title;
    private String contents;
    private String board;
    private String category;
    private int likeCount;
    private int viewCount;
    private LocalDateTime createdAt;

    public BoardPostOneResponseDto(BoardPost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.board = post.getBoardCategory().getBoard().getName();
        this.category = post.getBoardCategory().getName();
        this.likeCount = post.getLikeCount();
        this.viewCount = post.getViewCount();
        this.createdAt = post.getUpdatedAt();
    }
}
