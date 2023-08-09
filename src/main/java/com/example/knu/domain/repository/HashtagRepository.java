package com.example.knu.domain.repository;

import com.example.knu.domain.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    boolean existsByName(String name);

    List<Hashtag> findAllByNameIn(List<String> hashtags);
}
