package com.example.knu.controller;

import com.example.knu.common.Response;
import com.example.knu.dto.comment.request.CommentCreateRequestDto;
import com.example.knu.dto.comment.response.CommentCreateResponseDto;
import com.example.knu.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/user/boards/categories/{postId}/comment")
    public Response<CommentCreateResponseDto> createComment(@RequestBody CommentCreateRequestDto createDto,
                                                            @PathVariable Long postId,
                                                            Authentication authentication) {
        return Response.success(commentService.createComment(createDto, postId, authentication.getName()));
    }
}
