package com.example.knu.controller;

import com.example.knu.common.Response;
import com.example.knu.dto.comment.request.CommentCreateRequestDto;
import com.example.knu.dto.comment.request.CommentUpdateRequestDto;
import com.example.knu.dto.comment.response.CommentCreateResponseDto;
import com.example.knu.dto.comment.response.CommentDeleteResponseDto;
import com.example.knu.dto.comment.response.CommentListResponseDto;
import com.example.knu.dto.comment.response.CommentUpdateResponseDto;
import com.example.knu.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all/boards/categories/{postId}/comments")
    public Response<List<CommentListResponseDto>> findComment(@PathVariable Long postId) {

        return Response.success(commentService.findComment(postId));
    }

    @DeleteMapping("/user/boards/categories/{postId}/{commentId}")
    public Response<CommentDeleteResponseDto> deleteComment(@PathVariable Long postId,
                                                            @PathVariable Long commentId,
                                                            Authentication authentication) {
        return Response.success(commentService.deleteComment(postId, commentId, authentication.getName()));
    }

    @PatchMapping("/user/boards/categories/{postId}/{commentId}")
    public Response<CommentUpdateResponseDto> updateComment(@PathVariable Long postId,
                                                            @PathVariable Long commentId,
                                                            @RequestBody CommentUpdateRequestDto updateDto,
                                                            Authentication authentication) {
        return Response.success(commentService.updateComment(commentId, updateDto, authentication.getName()));
    }
}
