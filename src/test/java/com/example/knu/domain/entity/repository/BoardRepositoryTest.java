package com.example.knu.domain.entity.repository;

import com.example.knu.domain.entity.board.Board;
import com.example.knu.domain.repository.BoardCategoryRepository;
import com.example.knu.domain.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardCategoryRepository boardCategoryRepository;
    @BeforeEach
    void setUp() {
        boardCategoryRepository.deleteAll();
        boardRepository.deleteAll();
    }
    @DisplayName("게시판 생성 테스트")
    @Test
    void createBoard() {
        // given
        Board createCommunity = Board.builder()
                .name("커뮤니티")
                .priority(1)
                .build();
//
        Board createProject = Board.builder()
                .name("프로젝트")
                .priority(2)
                .build();
        // when
        boardRepository.save(createCommunity);
        boardRepository.save(createProject);

        // then
        List<Board> all = boardRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        assertThat(all.get(0).getName()).isEqualTo(createCommunity.getName());
        assertThat(all.get(1).getName()).isEqualTo(createProject.getName());

    }

}