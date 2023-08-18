package com.example.knu.dto.notice;

import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class NoticeCreation {

    @NotBlank
    private String title;
    @NotBlank
    private String contents;
    private List<MultipartFile> files;
    private List<String> hashtags;

    public BoardPost createBoardPost(User user, BoardCategory boardCategory) {
        return BoardPost.builder()
                .title(title)
                .contents(contents)
                .boardCategory(boardCategory)
                .viewCount(0)
                .likeCount(0)
                .user(user)
                .build();
    }
}
