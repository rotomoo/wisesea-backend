package com.example.knu.domain.entity.board;

import com.example.knu.domain.entity.BaseTimeEntity;
import com.example.knu.domain.entity.Comment;
import com.example.knu.domain.entity.user.User;
import com.example.knu.dto.board.request.BoardPostUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BoardPost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_post_id")
    private Long id;
    private String title;
    private String contents;
    private String thumbnailImageUrl;
    private int viewCount;
    private int likeCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_category_id")
    private BoardCategory boardCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "boardPost")
    private List<Comment> comments = new ArrayList<>();


    @Builder
    public BoardPost(String title, String contents, String thumbnailImageUrl, int viewCount, int likeCount, BoardCategory boardCategory, User user) {
        this.title = title;
        this.contents = contents;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.boardCategory = boardCategory;
        this.user = user;
    }

    public void plusBoardPostViewCount() {
        this.viewCount++;
    }

    public void updateThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public void updateBoardPost(BoardPostUpdateRequestDto updateDto, BoardCategory boardCategory) {
        this.title = updateDto.getTitle();
        this.contents = updateDto.getContents();
        this.boardCategory = boardCategory;
    }

    public void addLikeCount(int likeCount) {
        this.likeCount += likeCount;
    }
}
