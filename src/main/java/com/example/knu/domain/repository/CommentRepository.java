package com.example.knu.domain.repository;

import com.example.knu.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c " +
            "from Comment c " +
            "join fetch c.user " +
            "where c.boardPost.id =:boardPostId")
    List<Comment> findAllByBoardPostId(@Param("boardPostId") Long boardPostId);
}
