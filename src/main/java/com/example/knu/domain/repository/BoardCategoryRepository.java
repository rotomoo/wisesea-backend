package com.example.knu.domain.repository;

import com.example.knu.domain.entity.board.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {
    List<BoardCategory> findAllByBoardIdOrderByPriorityAsc(Long boardid);
}
