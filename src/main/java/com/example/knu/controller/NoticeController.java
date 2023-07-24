package com.example.knu.controller;

import com.example.knu.common.Response;
import com.example.knu.domain.entity.user.User;
import com.example.knu.dto.notice.NoticeCreation;
import com.example.knu.dto.notice.NoticeUpdate;
import com.example.knu.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 공지사항 등록
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/api/admin/boards/notices/post")
    public Response createNotice(@AuthenticationPrincipal User user,
                                 @ModelAttribute @Valid NoticeCreation noticeCreation) throws IOException {

        Response response = noticeService.createNotice(user, noticeCreation);

        return response;
    }

    /**
     * 공지사항 삭제
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/api/admin/boards/notices/{postid}")
    public Response deleteNotice(@PathVariable Long postid) {

        Response response = noticeService.deleteNotice(postid);

        return response;
    }

    /**
     * 공지사항 수정
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/api/admin/boards/notices/{postid}")
    public Response updateNotice(@PathVariable Long postid,
                                 @ModelAttribute @Valid NoticeCreation noticeCreation) throws IOException {

        Response response = noticeService.updateNotice(postid, noticeCreation);

        return response;
    }

    /**
     * 공지사항 좋아요
     */
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/api/user/boards/notices/{postid}/like")
    public Response likeNotice(Principal principal,
                               @PathVariable Long postid) {

        Response response = noticeService.likeNotice(principal, postid);

        return response;
    }
}
