package com.example.knu.dto.comment.request;

import com.example.knu.domain.entity.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentUpdateRequestDto {
    private String commentContents;

    public CommentUpdateRequestDto(Comment comment) {
        this.commentContents = comment.getCommentContents();
    }
}
