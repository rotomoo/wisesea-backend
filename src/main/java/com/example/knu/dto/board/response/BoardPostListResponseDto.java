package com.example.knu.dto.board.response;

import com.example.knu.domain.entity.board.BoardPost;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardPostListResponseDto {
    private Long id;
    private String title;
//    private String nickname;
    private int likeCount;
    private int viewCount;
    private LocalDateTime createdAt;

    public BoardPostListResponseDto(BoardPost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.likeCount = post.getLikeCount();
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt();
    }
}
