package com.example.knu.domain.repository;

import com.example.knu.domain.entity.board.Board;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByPriorityAsc();
}
