package com.example.knu.dto.board.request;

import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.entity.board.BoardPost;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardPostCreateRequestDto {
    private String title;
    private String contents;
    private String thumbnailImageUrl;
    @Builder
    public BoardPostCreateRequestDto(String title, String contents, String thumbnailImageUrl) {
        this.title = title;
        this.contents = contents;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public BoardPost toEntity(BoardCategory category) {
        return BoardPost.builder()
                .title(title)
                .contents(contents)
                .thumbnailImageUrl(thumbnailImageUrl)
                .boardCategory(category)
                .build();
    }

}
