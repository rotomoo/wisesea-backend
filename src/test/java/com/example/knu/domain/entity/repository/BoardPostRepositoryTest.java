package com.example.knu.domain.entity.repository;

import com.example.knu.domain.entity.board.Board;
import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.repository.BoardCategoryRepository;
import com.example.knu.domain.repository.BoardPostRepository;
import com.example.knu.domain.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardPostRepositoryTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardCategoryRepository boardCategoryRepository;
    @Autowired
    BoardPostRepository boardPostRepository;
    @BeforeEach
    void setUp() {
        boardPostRepository.deleteAll();
        boardCategoryRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @DisplayName("게시'글' 생성 테스트")
    @Test
    void createPost() {
        // given
        String boardName = "커뮤니티";
        Board board = createBoard(boardName);

        String categoryName = "고민상담";
        BoardCategory category = createCategory(categoryName, 1, "카테고리 설명", board);

        BoardPost post = BoardPost.builder()
                .title("제목")
                .contents("내용")
                .boardCategory(category)
                .thumbnailImageUrl("image")
                .build();

        boardPostRepository.save(post);

        // when
        List<BoardPost> all = boardPostRepository.findAll();
        BoardPost boardPost = all.get(0);

        // then
        assertThat(boardPost.getBoardCategory().getBoard().getName()).isEqualTo(boardName);
        assertThat(boardPost.getTitle()).isEqualTo("제목");
        assertThat(boardPost.getBoardCategory().getName()).isEqualTo(categoryName);

    }

    @DisplayName("커뮤니티/프로젝트 2개 생성 후 커뮤니티안에 카테고리 2개 생성 후 각각 게시글 조회")
    @Test
    void findAllPostByCategory() {
        // given
        String boardCommunity = "community";
        String boardProject = "project";
        Board community = createBoard(boardCommunity);
        Board project = createBoard(boardProject);

        String mentoring = "mentoring";
        String free = "free";
        BoardCategory createCategoryByMentoring = createCategory(mentoring, 1, "멘토링", community);
        BoardCategory createCategoryByFree = createCategory(free, 2, "자유", community);

        for (int i = 0; i < 5; i++) {
            BoardPost post = BoardPost.builder()
                    .title("자유" + i)
                    .contents("자유" + i)
                    .boardCategory(createCategoryByFree)
                    .thumbnailImageUrl("image")
                    .build();
            boardPostRepository.save(post);
        }

        for (int i = 0; i < 5; i++) {
            BoardPost post = BoardPost.builder()
                    .title("멘토링" + i)
                    .contents("멘토링" + i)
                    .boardCategory(createCategoryByMentoring)
                    .thumbnailImageUrl("image")
                    .build();
            boardPostRepository.save(post);
        }

        // when
        List<BoardPost> all = boardPostRepository.findAll();
        List<BoardPost> allByBoardCategoryMentoring = boardPostRepository.findAllByBoardCategoryId(createCategoryByMentoring.getId());
        List<BoardPost> allByBoardCategoryFree = boardPostRepository.findAllByBoardCategoryId(createCategoryByFree.getId());
        // then
        assertThat(all.size()).isEqualTo(10);

        assertThat(allByBoardCategoryMentoring.size()).isEqualTo(5);
        assertThat(allByBoardCategoryMentoring.get(0).getBoardCategory().getName()).isEqualTo(mentoring);

        assertThat(allByBoardCategoryFree.size()).isEqualTo(5);
        assertThat(allByBoardCategoryFree.get(0).getBoardCategory().getName()).isEqualTo(free);
    }

    private Board createBoard(String name) {
        Board createBoardCommunity = Board.builder()
                .name(name)
                .priority(1)
                .build();

        Board savedBoard = boardRepository.save(createBoardCommunity);

        return savedBoard;
    }

    private BoardCategory createCategory(String name, int priority, String description, Board board) {
        BoardCategory boardCategory = BoardCategory.builder()
                .name(name)
                .priority(priority)
//                .description(description)
                .board(board)
                .build();

        BoardCategory savedCategory = boardCategoryRepository.save(boardCategory);

        return savedCategory;
    }

}