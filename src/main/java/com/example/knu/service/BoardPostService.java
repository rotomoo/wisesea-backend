package com.example.knu.service;

import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import com.example.knu.domain.repository.BoardCategoryRepository;
import com.example.knu.domain.repository.BoardPostRepository;
import com.example.knu.domain.repository.UserRepository;
import com.example.knu.dto.board.request.BoardPostCreateRequestDto;
import com.example.knu.dto.board.request.BoardPostUpdateRequestDto;
import com.example.knu.dto.board.response.*;
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
    private final UserRepository userRepository;

    @Transactional
    public BoardPostCreateResponseDto createBoardPost(BoardPostCreateRequestDto postDto,
                                                      Long categoryId,
                                                      String username) {
        BoardCategory boardCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        User user = userRepository.findOneWithAuthoritiesByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        BoardPost boardPost = postRepository.save(postDto.toEntity(boardCategory, user));

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

    @Transactional
    public BoardPostDeleteResponseDto deleteBoardPost(Long postId, String username) {
        // 그 게시글이 내가 로그이한 계정과 일치하는지 확인
        BoardPost post = postRepository.findByBoardPostIdForDelete(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!post.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        postRepository.delete(post);
        return new BoardPostDeleteResponseDto(post);
    }

    @Transactional
    public BoardPostUpdateResponseDto updateBoardPost(Long postId, BoardPostUpdateRequestDto updateDto, String username) {
        BoardPost post = postRepository.findByBoardPostIdForUpdate(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!post.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        post.updateBoardPost(updateDto);
        return new BoardPostUpdateResponseDto(post);
    }


}
