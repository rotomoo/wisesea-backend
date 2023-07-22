package com.example.knu.domain.entity;

import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import com.example.knu.dto.comment.request.CommentUpdateRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_post_id")
    private BoardPost boardPost;

    @NotNull
    private String commentContents;

    @Builder
    public Comment(BoardPost boardPost, User user, String commentContents) {
        this.boardPost = boardPost;
        this.user = user;
        this.commentContents = commentContents;
    }

    public void updateComment(CommentUpdateRequestDto updateDto) {
        this.commentContents = updateDto.getCommentContents();
    }
}
