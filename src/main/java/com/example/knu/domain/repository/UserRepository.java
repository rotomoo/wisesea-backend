package com.example.knu.domain.repository;

import com.example.knu.domain.entity.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByNickname(String nickname);
}