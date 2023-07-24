package com.example.knu.domain.repository;

import com.example.knu.domain.entity.NoticeKnouOrigin;
import com.example.knu.domain.repository.custom.NoticeKnouOriginCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public interface NoticeKnouOriginRepository extends JpaRepository<NoticeKnouOrigin, Long>, NoticeKnouOriginCustom {
}
