package com.example.knu.controller;

import com.example.knu.common.Response;
import com.example.knu.domain.repository.BoardPostRepository;
import com.example.knu.dto.board.request.BoardPostCreateRequestDto;
import com.example.knu.dto.board.request.BoardPostUpdateRequestDto;
import com.example.knu.dto.board.request.BoardUnifiedPostsRequest;
import com.example.knu.dto.board.response.*;
import com.example.knu.service.BoardPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class BoardPostController {
    private final BoardPostService postService;
    private final BoardPostRepository postRepository;

    @PostMapping("/user/boards/{categoryId}/post")
    public Response<BoardPostCreateResponseDto> createBoardPost(@ModelAttribute BoardPostCreateRequestDto postDto,
                                                                @PathVariable Long categoryId,
                                                                Authentication authentication) throws IOException {
//        postService.createBoardPost(postDto, categoryId);
        log.info(authentication.getName());
        return Response.success(postService.createBoardPost(postDto, categoryId, authentication.getName()));
    }

//    @GetMapping("/all/boards/{categoryId}/posts")
//    public Response<List<BoardPostListResponseDto>> findBoardPost(@PathVariable Long categoryId) {
//        return Response.success(postService.findBoardPost(categoryId));
//    }

    @GetMapping("/all/boards/categories/{postId}")
    public Response<BoardPostOneResponseDto> findOneBoardPost(@PathVariable Long postId) {
        return Response.success(postService.findOneBoardPost(postId));
    }

    @DeleteMapping("/user/boards/categories/{postId}")
    public Response<BoardPostDeleteResponseDto> deleteBoardPost(@PathVariable Long postId,
                                                                Authentication authentication) {
        return Response.success(postService.deleteBoardPost(postId, authentication.getName()));
    }
    @PatchMapping("/user/boards/categories/{postId}")
    public Response<BoardPostUpdateResponseDto> updateBoardPost(@PathVariable Long postId,
                                                                @RequestBody BoardPostUpdateRequestDto updateDto,
                                                                Authentication authentication) throws IOException {
        return Response.success(postService.updateBoardPost(postId, updateDto, authentication.getName()));
    }

    /**
     * 게시판 목록 조회
     */
    @GetMapping("/all/boards")
    public Response getBoards() {

        Response response = postService.getBoards();

        return response;
    }

    /**
     * 게시판 카테고리 목록 조회
     */
    @GetMapping("/all/boards/{boardid}/categories")
    public Response getBoardCategories(@PathVariable Long boardid) {

        Response response = postService.getBoardCategories(boardid);

        return response;
    }

    /**
     * 페이징) 게시판 목록 조회
     */
    @GetMapping("/all/boards/{categoryId}/posts")
    public Response<Page<BoardPostListResponseDto>> findBoardPostByPaging(@PathVariable Long categoryId, Pageable pageable) {
        return Response.success(postRepository.findBoardPost(categoryId, pageable));
    }

    /**
     * 통합 검색 목록 조회
     */
    @GetMapping("/all/boards/posts/unified")
    public Response getUnifiedBoardPosts(@ModelAttribute @Valid BoardUnifiedPostsRequest boardUnifiedPostsRequest) {

        Response response = postService.getCategoryUnifiedBoardPosts(boardUnifiedPostsRequest);

        return response;
    }

    /**
     * 카테고리 게시글 통합 조회
     */
    @GetMapping("/all/boards/{categoryId}/posts/unified")
    public Response getCategoryUnifiedBoardPosts(@PathVariable Long categoryId,
                                         @ModelAttribute @Valid BoardUnifiedPostsRequest boardUnifiedPostsRequest) {

        boardUnifiedPostsRequest.setCategoryId(categoryId);

        Response response = postService.getCategoryUnifiedBoardPosts(boardUnifiedPostsRequest);

        return response;
    }

    /**
     * 게시판 게시글 통합 조회
     */
    @GetMapping("/all/{boardId}/categories/posts/unified")
    public Response getUnifiedBoardPosts(@PathVariable Long boardId,
                                         @ModelAttribute @Valid BoardUnifiedPostsRequest boardUnifiedPostsRequest) {

        boardUnifiedPostsRequest.setBoardId(boardId);

        Response response = postService.getCategoryUnifiedBoardPosts(boardUnifiedPostsRequest);

        return response;
    }
}
