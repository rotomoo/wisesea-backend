package com.example.knu.domain.repository;

import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.repository.custom.BoardPostCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long>, BoardPostCustom {

    @Query("select p " +
            "from BoardPost p " +
            "join fetch p.user " +
            "left join fetch p.boardCategory c " +
            "where p.boardCategory.id =:categoryId")
    List<BoardPost> findAllByBoardCategoryId(@Param("categoryId") Long categoryId);

    @Query("select p " +
            "from BoardPost p " +
            "join fetch p.user " +
            "left join fetch p.boardCategory c " +
            "left join fetch c.board b " +
            "where p.id =:postId")
    Optional<BoardPost> findByBoardPostId(@Param("postId") Long postId);

    /**
     * 게시글 삭제할 때 하나 불러오는 쿼리
     */
    @Query("select p " +
            "from BoardPost p " +
            "join fetch p.user " +
            "where p.id =:postId")
    Optional<BoardPost> findByBoardPostIdForDelete(@Param("postId") Long postId);
    /**
     * 게시글 삭제할 때 하나 불러오는 쿼리
     */
    @Query("select p " +
            "from BoardPost p " +
            "join fetch p.user " +
            "where p.id =:postId")
    Optional<BoardPost> findByBoardPostIdForUpdate(@Param("postId") Long postId);
}
