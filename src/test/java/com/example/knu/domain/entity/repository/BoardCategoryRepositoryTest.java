package com.example.knu.domain.entity.repository;

import com.example.knu.domain.entity.board.Board;
import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.repository.BoardCategoryRepository;
import com.example.knu.domain.repository.BoardPostRepository;
import com.example.knu.domain.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BoardCategoryRepositoryTest {
    @Autowired
    BoardCategoryRepository boardCategoryRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardPostRepository boardPostRepository;
    @BeforeEach
    void setUp() {
        boardPostRepository.deleteAll();
        boardCategoryRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @DisplayName("카테고리 생성 테스트")
    @Test
    void createCategory() {
        // given
        // Category : Board 는 N : 1 관계이기 때문에 Board 값이 존재해야 된다.
        String name = "커뮤니티";
        Board savedBoard = createBoard(name);

        BoardCategory createCategory = BoardCategory.builder()
                .name("고민상담")
                .priority(1)
//                .description("커뮤니티에 대한 설명입니다.")
                .board(savedBoard)
                .build();

        boardCategoryRepository.save(createCategory);

        // when
        List<BoardCategory> all = boardCategoryRepository.findAll();
        BoardCategory boardCategory = all.get(0);

        // then
        assertThat(boardCategory.getName()).isEqualTo(createCategory.getName());
        assertThat(boardCategory.getBoard().getName()).isEqualTo(name);

    }

    private Board createBoard(String name) {
        Board createBoardCommunity = Board.builder()
                .name(name)
                .priority(1)
                .build();

        Board savedBoard = boardRepository.save(createBoardCommunity);

        return savedBoard;
    }


}