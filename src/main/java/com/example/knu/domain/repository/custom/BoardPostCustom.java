package com.example.knu.domain.repository.custom;

import com.example.knu.domain.entity.board.BoardPost;

public interface BoardPostCustom {
    void deleteByQuerydsl(BoardPost boardPost);

    void deleteFileHashtagByQuerydsl(BoardPost boardPost);
}
