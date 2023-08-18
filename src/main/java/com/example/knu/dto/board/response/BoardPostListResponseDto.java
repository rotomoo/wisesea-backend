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
    private String nickname;
    private String profileImage;
    private int likeCount;
    private int viewCount;
    private int commentCount;
    private LocalDateTime createdAt;

    public BoardPostListResponseDto(BoardPost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.nickname = post.getUser().getNickname();
        this.profileImage = post.getUser().getProfileImageUrl();
        this.likeCount = post.getLikeCount();
        this.viewCount = post.getViewCount();
        this.commentCount = post.getComments().size();
        this.createdAt = post.getCreatedAt();
    }
}
