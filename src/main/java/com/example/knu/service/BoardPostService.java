package com.example.knu.service;

import com.example.knu.dto.board.request.BoardPostCreateRequestDto;
import com.example.knu.dto.board.response.BoardPostCreateResponseDto;
import com.example.knu.dto.board.response.BoardPostListResponseDto;
import com.example.knu.dto.board.response.BoardPostOneResponseDto;
import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.repository.BoardCategoryRepository;
import com.example.knu.repository.BoardPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardPostService {
    private final BoardPostRepository postRepository;
    private final BoardCategoryRepository categoryRepository;

    @Transactional
    public BoardPostCreateResponseDto createBoardPost(BoardPostCreateRequestDto postDto,
                                                      Long categoryId) {
        BoardCategory boardCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        BoardPost boardPost = postRepository.save(postDto.toEntity(boardCategory));

        return new BoardPostCreateResponseDto(boardPost);
    }

    public List<BoardPostListResponseDto> findBoardPost(Long categoryId) {
        List<BoardPost> boardPostsByCategoryId = postRepository.findAllByBoardCategoryId(categoryId);

        List<BoardPostListResponseDto> boardPostListResponseDtos = boardPostsByCategoryId.stream()
                .map(BoardPostListResponseDto::new)
                .collect(Collectors.toList());

        return boardPostListResponseDtos;
    }

    @Transactional
    public BoardPostOneResponseDto findOneBoardPost(Long categoryId, Long postId) {
        BoardPost byBoardPostId = postRepository.findByBoardPostId(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        byBoardPostId.plusBoardPostViewCount();

        return new BoardPostOneResponseDto(byBoardPostId);
    }


}
