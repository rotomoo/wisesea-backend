package com.example.knu.dto.comment.response;

import com.example.knu.domain.entity.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentUpdateResponseDto {
    private Long id;
    private String commentContents;

    public CommentUpdateResponseDto(Comment comment) {
        this.id = comment.getId();
        this.commentContents = comment.getCommentContents();
    }
}
