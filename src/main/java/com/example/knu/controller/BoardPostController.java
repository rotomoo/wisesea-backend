package com.example.knu.controller;

import com.example.knu.common.Response;
import com.example.knu.dto.board.request.BoardPostCreateRequestDto;
import com.example.knu.dto.board.request.BoardPostUpdateRequestDto;
import com.example.knu.dto.board.response.*;
import com.example.knu.service.BoardPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class BoardPostController {
    private final BoardPostService postService;

    @PostMapping("/user/boards/{categoryId}/post")
    public Response<BoardPostCreateResponseDto> createBoardPost(@RequestBody BoardPostCreateRequestDto postDto,
                                                                @PathVariable Long categoryId,
                                                                Authentication authentication) {
//        postService.createBoardPost(postDto, categoryId);
        log.info(authentication.getName());
        return Response.success(postService.createBoardPost(postDto, categoryId, authentication.getName()));
    }

    @GetMapping("/all/boards/{categoryId}/posts")
    public Response<List<BoardPostListResponseDto>> findBoardPost(@PathVariable Long categoryId) {
        return Response.success(postService.findBoardPost(categoryId));
    }

    /*
    {categoryId} 빼도 됨
     */
    @GetMapping("/all/boards/{categoryId}/{postId}")
    public Response<BoardPostOneResponseDto> findOneBoardPost(@PathVariable Long categoryId,
                                                              @PathVariable Long postId) {
        return Response.success(postService.findOneBoardPost(categoryId, postId));
    }

    @DeleteMapping("/user/boards/categories/{postId}")
    public Response<BoardPostDeleteResponseDto> deleteBoardPost(@PathVariable Long postId,
                                                                Authentication authentication) {
        return Response.success(postService.deleteBoardPost(postId, authentication.getName()));
    }
    @PatchMapping("/user/boards/categories/{postId}")
    public Response<BoardPostUpdateResponseDto> updateBoardPost(@PathVariable Long postId,
                                                                @RequestBody BoardPostUpdateRequestDto updateDto,
                                                                Authentication authentication) {
        return Response.success(postService.updateBoardPost(postId, updateDto, authentication.getName()));
    }
}
