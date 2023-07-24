package com.example.knu.domain.repository.custom;

import com.example.knu.domain.mapping.CollegeNoticesMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeKnouOriginCustom {
    Page<CollegeNoticesMapping> findAllByQuerydsl(Pageable pageable);
}
