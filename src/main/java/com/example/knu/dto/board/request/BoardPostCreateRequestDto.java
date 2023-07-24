package com.example.knu.dto.board.request;

import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardPostCreateRequestDto {
    private String title;
    private String contents;
    @Builder
    public BoardPostCreateRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public BoardPost toEntity(BoardCategory category, User user) {
        return BoardPost.builder()
                .title(title)
                .contents(contents)
                .boardCategory(category)
                .user(user)
                .build();
    }

}
