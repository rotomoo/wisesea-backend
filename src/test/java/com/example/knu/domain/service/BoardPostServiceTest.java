package com.example.knu.domain.service;

import com.example.knu.domain.dto.board.request.BoardPostCreateRequestDto;
import com.example.knu.domain.entity.board.Board;
import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.repository.BoardCategoryRepository;
import com.example.knu.domain.repository.BoardPostRepository;
import com.example.knu.domain.repository.BoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardPostServiceTest {
    @Autowired
    BoardPostService boardPostService;
    @Autowired
    BoardPostRepository boardPostRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardCategoryRepository boardCategoryRepository;
    Board board;
    BoardCategory boardCategory;

    @BeforeEach
    void setUp() {
        boardRepository.deleteAll();
        boardPostRepository.deleteAll();
        boardCategoryRepository.deleteAll();

        board = boardRepository.save(Board.builder()
                .name("커뮤니티")
                .priority(1)
                .description("커뮤니티입니다.")
                .build());
        boardCategory = boardCategoryRepository.save(BoardCategory.builder()
                .board(board)
                .name("고민상다")
                .priority(1)
                .build());
    }

    @DisplayName("게시글 작성 기능 테스트 (커뮤니티)")
    @Transactional
    @Test
    void createPostInCommunity() {
        // given
        BoardPostCreateRequestDto createDto = BoardPostCreateRequestDto.builder()
                .title("제목")
                .contents("내용")
                .build();

        boardPostService.createBoardPost(createDto, boardCategory.getId());

        // when
        List<BoardPost> findPost = boardPostRepository.findAll();

        // then
        assertThat(findPost.get(0).getBoardCategory().getName()).isEqualTo(boardCategory.getName());
        assertThat(findPost.get(0).getTitle()).isEqualTo(createDto.getTitle());
    }

}