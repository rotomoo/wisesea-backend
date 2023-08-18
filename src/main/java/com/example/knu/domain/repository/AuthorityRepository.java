package com.example.knu.domain.repository;

import com.example.knu.domain.entity.user.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
