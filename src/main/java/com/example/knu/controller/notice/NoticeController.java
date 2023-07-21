package com.example.knu.controller.notice;

import com.example.knu.common.Response;
import com.example.knu.domain.entity.user.User;
import com.example.knu.dto.notice.NoticeCreation;
import com.example.knu.service.notice.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 공지사항 등록
     */
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/api/admin/boards/notices/post")
    public Response createNotice(@AuthenticationPrincipal User user,
                                 @ModelAttribute @Valid NoticeCreation noticeCreation) throws IOException {

        Response response = noticeService.createNotice(user, noticeCreation);

        return response;
    }

    /**
     * 공지사항 삭제
     */
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/api/admin/boards/notices/{postid}")
    public Response deleteNotice(@PathVariable Long postid) {

        Response response = noticeService.deleteNotice(postid);

        return response;
    }

}
