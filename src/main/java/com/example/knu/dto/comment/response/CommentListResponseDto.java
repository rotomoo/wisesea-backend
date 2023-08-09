package com.example.knu.dto.comment.response;

import com.example.knu.domain.entity.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentListResponseDto {
    private Long id;
    private String loginId;
    private String nickname;
    private String commentContents;
    private String image;
    private LocalDateTime createdAt;

    public CommentListResponseDto(Comment comment) {
        this.id = comment.getId();
        this.loginId = comment.getUser().getLoginId();
        this.nickname = comment.getUser().getNickname();
        this.commentContents = comment.getCommentContents();
        this.image = comment.getUser().getProfileImageUrl();
        this.createdAt = comment.getCreatedAt();
    }
}
