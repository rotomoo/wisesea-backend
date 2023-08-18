package com.example.knu.dto.board.request;

import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class BoardPostCreateRequestDto {
    private String title;
    private String contents;
    private List<MultipartFile> files;

    public BoardPost toEntity(BoardCategory category, User user) {
        return BoardPost.builder()
                .title(title)
                .contents(contents)
                .boardCategory(category)
                .user(user)
                .build();
    }

}
