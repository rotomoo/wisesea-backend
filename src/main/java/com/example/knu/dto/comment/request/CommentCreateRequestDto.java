package com.example.knu.dto.comment.request;

import com.example.knu.domain.entity.Comment;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentCreateRequestDto {
    private String commentContents;

    @Builder
    public CommentCreateRequestDto(String commentContents) {
        this.commentContents = commentContents;
    }

    public Comment toEntity(BoardPost boardPost, User user, String commentContents) {
        return Comment.builder()
                .boardPost(boardPost)
                .user(user)
                .commentContents(commentContents)
                .build();
    }
}
