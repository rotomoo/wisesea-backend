package com.example.knu.domain.repository;

import com.example.knu.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProfileRepository extends JpaRepository<User, Long> {

    /**
     * username로 회원 찾기
     * @param username 회원 이메일
     * @return 회원 정보
     */
    Optional<User> findByUsername(String username);

    /**
     * 유효성 검사 - 중복 체크
     * @param username 회원 이메일
     * @return 참/거짓
     */
    boolean existsByUsername(String username);
}
