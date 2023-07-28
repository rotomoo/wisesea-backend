package com.example.knu.service;

import com.example.knu.common.PagingResponse;
import com.example.knu.common.Response;
import com.example.knu.common.s3.S3Directory;
import com.example.knu.common.s3.S3Uploader;
import com.example.knu.domain.entity.File;
import com.example.knu.domain.entity.board.Board;
import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import com.example.knu.domain.mapping.BoardUnifiedPostMapping;
import com.example.knu.domain.mapping.CollegeNoticesMapping;
import com.example.knu.domain.repository.*;
import com.example.knu.dto.board.request.BoardPostCreateRequestDto;
import com.example.knu.dto.board.request.BoardPostUpdateRequestDto;
import com.example.knu.dto.board.request.BoardUnifiedPostsRequest;
import com.example.knu.dto.board.response.*;
import com.example.knu.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardPostService {
    private final BoardPostRepository postRepository;
    private final BoardCategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final S3Uploader s3Uploader;
    private final FileRepository fileRepository;

    @Transactional
    public BoardPostCreateResponseDto createBoardPost(BoardPostCreateRequestDto postDto,
                                                      Long categoryId,
                                                      String username) throws IOException {
        BoardCategory boardCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        User user = userRepository.findOneWithAuthoritiesByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        BoardPost boardPost = postRepository.save(postDto.toEntity(boardCategory, user));

        if (postDto.getFiles() != null && !postDto.getFiles().isEmpty()) {
            List<MultipartFile> files = postDto.getFiles();
            if (files.size() > 5) throw new CommonException("파일은 5개까지 등록 가능합니다");

            for (MultipartFile multipartFile : files) {
                String fileUrl = s3Uploader.uploadFileToS3(multipartFile,
                        S3Directory.BOARD.getPath() + boardPost.getId() + S3Directory.FILES.getPath() + multipartFile.getOriginalFilename());

                File file = File.builder()
                        .boardPost(boardPost)
                        .url(fileUrl)
                        .build();
                fileRepository.save(file);
            }
        }

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
    public BoardPostUpdateResponseDto updateBoardPost(Long postId, BoardPostUpdateRequestDto updateDto, String username) throws IOException {
        BoardPost post = postRepository.findByBoardPostIdForUpdate(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!post.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        BoardCategory boardCategory = categoryRepository.findById(updateDto.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        post.updateBoardPost(updateDto, boardCategory);

        if (updateDto.getFiles() != null && !updateDto.getFiles().isEmpty()) {
            List<MultipartFile> files = updateDto.getFiles();
            if (files.size() > 5) throw new CommonException("파일은 5개까지 등록 가능합니다");

            for (MultipartFile multipartFile : files) {
                String fileUrl = s3Uploader.uploadFileToS3(multipartFile,
                        S3Directory.BOARD.getPath() + post.getId() + S3Directory.FILES.getPath());

                File file = File.builder()
                        .boardPost(post)
                        .url(fileUrl)
                        .build();
                fileRepository.save(file);
            }
        }

        return new BoardPostUpdateResponseDto(post);
    }

    /**
     * 게시판 목록 조회
     * @return
     */
    public Response getBoards() {
        List<Board> boards = boardRepository.findAllByOrderByPriorityAsc();

        BoardResponseDto boardResponseDto = new BoardResponseDto();

        List<BoardResponseDto.BoardDto> collect =
                boards.stream().map(board -> new BoardResponseDto.BoardDto(
                board.getId(),
                board.getName(),
                board.getDescription()
        )).collect(Collectors.toList());

        boardResponseDto.setList(collect);

        return Response.success(boardResponseDto);
    }

    /**
     * 게시판 카테고리 목록 조회
     * @param boardid
     * @return
     */
    public Response getBoardCategories(Long boardid) {
        List<BoardCategory> boardCategories = categoryRepository.findAllByBoardIdOrderByPriorityAsc(boardid);

        BoardCategoriesResponseDto boardCategoriesResponseDto = new BoardCategoriesResponseDto();

        List<BoardCategoriesResponseDto.BoardCategoriesDto> collect =
                boardCategories.stream().map(boardCategory -> new BoardCategoriesResponseDto.BoardCategoriesDto(
                boardCategory.getId(),
                boardCategory.getName()
        )).collect(Collectors.toList());

        boardCategoriesResponseDto.setList(collect);

        return Response.success(boardCategoriesResponseDto);
    }

    /**
     * 통합 검색
     * @param boardUnifiedPostsRequest
     * @return
     */
    public Response getUnifiedBoardPosts(BoardUnifiedPostsRequest boardUnifiedPostsRequest) {
        String direction = boardUnifiedPostsRequest.getSort().getDirection();
        String columnName = boardUnifiedPostsRequest.getSort().getColumnName();
        Sort sort = Sort.by(Sort.Direction.valueOf(direction), columnName);
        PageRequest pageable = PageRequest.of(boardUnifiedPostsRequest.getPageNumber() - 1, boardUnifiedPostsRequest.getPageSize(), sort);

        Page<BoardUnifiedPostMapping> boardUnifiedPostMappingPage =
                postRepository.findAllByQuerydsl(boardUnifiedPostsRequest.getCategoryId(), boardUnifiedPostsRequest.getInput(), pageable);

        return Response.success(new BoardUnifiedPostsResponse(
                PagingResponse.createPagingInfo(boardUnifiedPostMappingPage), boardUnifiedPostMappingPage.getContent()
        ));
    }
}
