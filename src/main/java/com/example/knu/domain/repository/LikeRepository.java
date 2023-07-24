package com.example.knu.domain.repository;

import com.example.knu.domain.entity.Like;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndBoardPost(User user, BoardPost boardPost);

    void deleteByUserAndBoardPost(User user, BoardPost boardPost);
}
