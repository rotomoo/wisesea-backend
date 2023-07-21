package com.example.knu.service.notice;

import com.example.knu.domain.repository.BoardPostRepository;
import com.example.knu.domain.repository.notice.NoticeKnouOriginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeKnouOriginRepository noticeKnouOriginRepository;

    private final BoardPostRepository boardPostRepository;
}
