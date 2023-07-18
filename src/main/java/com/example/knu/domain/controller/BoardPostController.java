package com.example.knu.domain.controller;

import com.example.knu.domain.dto.Response;
import com.example.knu.domain.dto.board.request.BoardPostCreateRequestDto;
import com.example.knu.domain.dto.board.response.BoardPostCreateResponseDto;
import com.example.knu.domain.dto.board.response.BoardPostListResponseDto;
import com.example.knu.domain.dto.board.response.BoardPostOneResponseDto;
import com.example.knu.domain.service.BoardPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BoardPostController {
    private final BoardPostService postService;

    @PostMapping("/boards/{categoryId}/post")
    public Response<BoardPostCreateResponseDto> createBoardPost(@RequestBody BoardPostCreateRequestDto postDto,
                                                                @PathVariable Long categoryId) {
//        postService.createBoardPost(postDto, categoryId);
        return Response.success(postService.createBoardPost(postDto, categoryId));
    }

    @GetMapping("/boards/{categoryId}/posts")
    public Response<List<BoardPostListResponseDto>> findBoardPost(@PathVariable Long categoryId) {
        return Response.success(postService.findBoardPost(categoryId));
    }

    @GetMapping("/boards/{categoryId}/{postId}")
    public Response<BoardPostOneResponseDto> findOneBoardPost(@PathVariable Long categoryId,
                                                              @PathVariable Long postId) {
        return Response.success(postService.findOneBoardPost(categoryId, postId));
    }



}
