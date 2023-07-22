package com.example.knu.service;

import com.example.knu.domain.entity.Comment;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import com.example.knu.domain.repository.BoardPostRepository;
import com.example.knu.domain.repository.CommentRepository;
import com.example.knu.domain.repository.UserRepository;
import com.example.knu.dto.comment.request.CommentCreateRequestDto;
import com.example.knu.dto.comment.response.CommentCreateResponseDto;
import com.example.knu.dto.comment.response.CommentListResponseDto;
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
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardPostRepository postRepository;

    @Transactional
    public CommentCreateResponseDto createComment(CommentCreateRequestDto createDto, Long postId, String username) {
        BoardPost boardPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = userRepository.findOneWithAuthoritiesByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Comment comment = commentRepository.save(createDto.toEntity(boardPost, user, createDto.getCommentContents()));
        return new CommentCreateResponseDto(comment);
    }

    public List<CommentListResponseDto> findComment(Long postId) {
//        List<Comment> allByPostId = commentRepository.findAllByPostId(postId);

//        List<CommentListResponseDto> commentListResponseDto = allByPostId.stream()
//                .map(CommentListResponseDto::new)
//                .collect(Collectors.toList());
        List<Comment> allComment = commentRepository.findAllByBoardPostId(postId);
        List<CommentListResponseDto> commentListResponseDto = allComment.stream()
                .map(CommentListResponseDto::new)
                .collect(Collectors.toList());
        return commentListResponseDto;
    }
}
