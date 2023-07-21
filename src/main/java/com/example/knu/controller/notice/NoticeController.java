package com.example.knu.controller.notice;

import com.example.knu.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/api/admin/boards/notices/post")
    public ResponseEntity<?> createNotice() {

//        noticeService.createNotice()

        return null;
    }

}
