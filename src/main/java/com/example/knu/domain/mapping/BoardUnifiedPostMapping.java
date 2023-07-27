package com.example.knu.domain.mapping;

import com.example.knu.domain.entity.enums.UserType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardUnifiedPostMapping {
    private Long boardId;
    private String boardName;
    private Long boardCategoryId;
    private String boardCategoryName;
    private Long boardPostId;
    private Long userId;
    private String username;
    private UserType userType;
    private String nickname;
    private String profileImageUrl;
    private String title;
    private String contents;
    private Integer viewCount;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
