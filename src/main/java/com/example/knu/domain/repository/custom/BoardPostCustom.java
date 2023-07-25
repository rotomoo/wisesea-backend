package com.example.knu.domain.repository.custom;

import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.dto.board.response.BoardPostListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardPostCustom {
    void deleteByQuerydsl(BoardPost boardPost);

    void deleteFileHashtagByQuerydsl(BoardPost boardPost);
    Page<BoardPostListResponseDto> findBoardPost(Long categoryId, Pageable pageable);
}
