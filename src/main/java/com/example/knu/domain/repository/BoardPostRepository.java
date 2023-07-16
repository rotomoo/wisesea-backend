package com.example.knu.domain.repository;

import com.example.knu.domain.entity.board.BoardPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {

    List<BoardPost> findAllByBoardCategoryId(Long categoryId);
}
