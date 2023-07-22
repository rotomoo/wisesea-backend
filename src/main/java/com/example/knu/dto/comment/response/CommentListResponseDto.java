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
    private String nickname;
    private String commentContents;
    private LocalDateTime createdAt;

    public CommentListResponseDto(Comment comment) {
        this.id = comment.getId();
        this.nickname = comment.getUser().getNickname();
        this.commentContents = comment.getCommentContents();
        this.createdAt = comment.getCreatedAt();
    }
//    public CommentListResponseDto(Comment comment) {
//        this.id = comment.getId();
//        this.commentContents = comment.getCommentContents();
//        this.createdAt = comment.getCreatedAt();
//    }
}
