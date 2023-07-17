package com.example.knu.domain.service;

import com.example.knu.domain.dto.board.request.BoardPostCreateRequestDto;
import com.example.knu.domain.dto.board.response.BoardPostCreateResponseDto;
import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.repository.BoardCategoryRepository;
import com.example.knu.domain.repository.BoardPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class BoardPostService {
    private final BoardPostRepository postRepository;
    private final BoardCategoryRepository categoryRepository;

    public BoardPostCreateResponseDto createBoardPost(BoardPostCreateRequestDto postDto,
                                                      Long categoryId) {
        BoardCategory boardCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        BoardPost boardPost = postRepository.save(postDto.toEntity(boardCategory));

        return new BoardPostCreateResponseDto(boardPost);
    }
}
