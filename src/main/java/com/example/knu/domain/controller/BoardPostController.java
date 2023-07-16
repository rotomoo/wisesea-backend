package com.example.knu.domain.controller;

import com.example.knu.domain.dto.Response;
import com.example.knu.domain.dto.board.request.BoardPostCreateRequestDto;
import com.example.knu.domain.dto.board.response.BoardPostCreateResponseDto;
import com.example.knu.domain.service.BoardPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
