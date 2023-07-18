package com.example.knu.repository;

import com.example.knu.domain.entity.User.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
