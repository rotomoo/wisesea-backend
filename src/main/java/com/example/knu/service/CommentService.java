package com.example.knu.service;

import com.example.knu.domain.entity.Comment;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import com.example.knu.domain.repository.BoardPostRepository;
import com.example.knu.domain.repository.CommentRepository;
import com.example.knu.domain.repository.UserRepository;
import com.example.knu.dto.comment.request.CommentCreateRequestDto;
import com.example.knu.dto.comment.request.CommentUpdateRequestDto;
import com.example.knu.dto.comment.response.CommentCreateResponseDto;
import com.example.knu.dto.comment.response.CommentDeleteResponseDto;
import com.example.knu.dto.comment.response.CommentListResponseDto;
import com.example.knu.dto.comment.response.CommentUpdateResponseDto;
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

    @Transactional
    public CommentDeleteResponseDto deleteComment(Long postId, Long commentId, String username) {
        BoardPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (comment.getBoardPost().getId() != postId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        User user = comment.getUser();
        if (!user.getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
//        User user = userRepository.findOneWithAuthoritiesByUsername(username).get();
//        List<Comment> byUser = commentRepository.findByUser(user);
//        for (Comment comment1 : byUser) {
//            System.out.println(comment1);
//        }


        commentRepository.delete(comment);
        return new CommentDeleteResponseDto(comment);
    }

    @Transactional
    public CommentUpdateResponseDto updateComment(Long commentId, CommentUpdateRequestDto updateDto, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        comment.updateComment(updateDto);
        return new CommentUpdateResponseDto(comment);
    }
}
