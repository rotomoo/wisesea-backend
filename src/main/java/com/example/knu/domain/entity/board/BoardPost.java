package com.example.knu.domain.entity.board;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BoardPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String contents;
    private String thumbnailImageUrl;
    private int viewCount;
    private int likeCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_category_id")
    private BoardCategory boardCategory;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    @Builder
    public BoardPost(String title, String contents, String thumbnailImageUrl, BoardCategory boardCategory) {
        this.title = title;
        this.contents = contents;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.viewCount = 0;
        this.likeCount = 0;
        this.boardCategory = boardCategory;
    }
}
