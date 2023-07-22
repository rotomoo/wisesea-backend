package com.example.knu.dto.comment.response;

import com.example.knu.domain.entity.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentCreateResponseDto {
    private Long id;
    private String commentContents;

    public CommentCreateResponseDto(Comment comment) {
        this.id = comment.getId();
        this.commentContents = comment.getCommentContents();
    }
}
