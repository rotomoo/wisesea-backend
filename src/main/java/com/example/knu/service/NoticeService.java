package com.example.knu.service;

import com.example.knu.common.PagingResponse;
import com.example.knu.common.PagingUtil;
import com.example.knu.common.Response;
import com.example.knu.common.s3.S3Directory;
import com.example.knu.common.s3.S3Uploader;
import com.example.knu.domain.entity.BoardPostHashtag;
import com.example.knu.domain.entity.File;
import com.example.knu.domain.entity.Hashtag;
import com.example.knu.domain.entity.Like;
import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import com.example.knu.domain.mapping.CollegeNoticesMapping;
import com.example.knu.domain.repository.*;
import com.example.knu.domain.repository.NoticeKnouOriginRepository;
import com.example.knu.dto.notice.CollegeNoticesRequest;
import com.example.knu.dto.notice.CollegeNoticesResponse;
import com.example.knu.dto.notice.NoticeCreation;
import com.example.knu.dto.notice.NoticeUpdate;
import com.example.knu.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeKnouOriginRepository noticeKnouOriginRepository;
    private final BoardPostRepository boardPostRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final S3Uploader s3Uploader;
    private final FileRepository fileRepository;
    private final HashtagRepository hashtagRepository;
    private final BoardPostHashtagRepository boardPostHashtagRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    /**
     * 공지사항 등록
     * @param user
     * @param noticeCreation
     * @return
     */
    @Transactional
    public Response createNotice(User user, NoticeCreation noticeCreation) throws IOException {
        Optional<BoardCategory> foundBoardCategory = boardCategoryRepository.findById(6L);
        BoardCategory boardCategory = foundBoardCategory.get();

        BoardPost boardPost = noticeCreation.createBoardPost(user, boardCategory);
        boardPostRepository.save(boardPost);

        // TODO 로그인 기능 되면 유저 프로필 이미지 넣기
        boardPost.updateThumbnailImageUrl(null);

        if (noticeCreation.getHashtags() != null && !noticeCreation.getHashtags().isEmpty()) {
            List<String> hashtags = noticeCreation.getHashtags();
            if (hashtags.size() > 5) throw new CommonException("해시태그는 5개까지 입력 가능합니다");

            for (String inputHashtag : hashtags) {
                if (!hashtagRepository.existsByName(inputHashtag)) {
                    Hashtag hashtag = Hashtag.builder()
                            .name(inputHashtag)
                            .build();

                    hashtagRepository.save(hashtag);
                }
            }

            List<Hashtag> targetHashtags = hashtagRepository.findAllByNameIn(hashtags);
            for (Hashtag targetHashtag : targetHashtags) {

                BoardPostHashtag boardPostHashtag = BoardPostHashtag.builder()
                        .boardPost(boardPost)
                        .hashtag(targetHashtag)
                        .build();

                boardPostHashtagRepository.save(boardPostHashtag);
            }
        }

        if (noticeCreation.getFiles() != null && !noticeCreation.getFiles().isEmpty()) {
            List<MultipartFile> files = noticeCreation.getFiles();
            if (files.size() > 5) throw new CommonException("파일은 5개까지 등록 가능합니다");

            for (MultipartFile multipartFile : files) {
                String fileUrl = s3Uploader.uploadFileToS3(multipartFile,
                        S3Directory.BOARD.getPath() + boardPost.getId() + S3Directory.FILES.getPath());

                File file = File.builder()
                        .boardPost(boardPost)
                        .url(fileUrl)
                        .build();
                fileRepository.save(file);
            }
        }

        return Response.success(null);
    }

    /**
     * 공지사항 삭제
     * @param postid
     * @return
     */
    @Transactional
    public Response deleteNotice(Long postid) {
        Optional<BoardPost> foundBoardPost = boardPostRepository.findById(postid);
        if (foundBoardPost.isEmpty()) throw new CommonException("해당 게시글을 찾을수 없습니다.");

        BoardPost boardPost = foundBoardPost.get();
        boardPostRepository.deleteByQuerydsl(boardPost);

        return Response.success(null);
    }

    /**
     * 공지사항 수정
     * @param postid
     * @param noticeCreation
     * @return
     */
    @Transactional
    public Response updateNotice(Long postid, NoticeCreation noticeCreation) throws IOException {
        Optional<BoardPost> foundBoardPost = boardPostRepository.findById(postid);
        if (foundBoardPost.isEmpty()) throw new CommonException("해당 게시글을 찾을수 없습니다.");

        BoardPost boardPost = foundBoardPost.get();

        boardPostRepository.deleteFileHashtagByQuerydsl(boardPost);

        if (noticeCreation.getHashtags() != null && !noticeCreation.getHashtags().isEmpty()) {
            List<String> hashtags = noticeCreation.getHashtags();
            if (hashtags.size() > 5) throw new CommonException("해시태그는 5개까지 입력 가능합니다");

            for (String inputHashtag : hashtags) {
                if (!hashtagRepository.existsByName(inputHashtag)) {
                    Hashtag hashtag = Hashtag.builder()
                            .name(inputHashtag)
                            .build();

                    hashtagRepository.save(hashtag);
                }
            }

            List<Hashtag> targetHashtags = hashtagRepository.findAllByNameIn(hashtags);
            for (Hashtag targetHashtag : targetHashtags) {

                BoardPostHashtag boardPostHashtag = BoardPostHashtag.builder()
                        .boardPost(boardPost)
                        .hashtag(targetHashtag)
                        .build();

                boardPostHashtagRepository.save(boardPostHashtag);
            }
        }

        if (noticeCreation.getFiles() != null && !noticeCreation.getFiles().isEmpty()) {
            List<MultipartFile> files = noticeCreation.getFiles();
            if (files.size() > 5) throw new CommonException("파일은 5개까지 등록 가능합니다");

            for (MultipartFile multipartFile : files) {
                String fileUrl = s3Uploader.uploadFileToS3(multipartFile,
                        S3Directory.BOARD.getPath() + boardPost.getId() + S3Directory.FILES.getPath());

                File file = File.builder()
                        .boardPost(boardPost)
                        .url(fileUrl)
                        .build();
                fileRepository.save(file);
            }
        }

        return Response.success(null);
    }

    /**
     * 게시글 좋아요
     * @param principal
     * @param postid
     * @return
     */
    @Transactional
    public Response likeNotice(Principal principal, Long postid) {
        Optional<BoardPost> foundBoardPost = boardPostRepository.findById(postid);
        if (foundBoardPost.isEmpty()) throw new CommonException("해당 공지사항이 존재하지 않습니다.");
        BoardPost boardPost = foundBoardPost.get();

        Optional<User> loginUser = userRepository.findByLoginId(principal.getName());
        User user = loginUser.get();

        Optional<Like> foundLike = likeRepository.findByUserAndBoardPost(user, boardPost);

        if (foundLike.isEmpty()) {
            Like like = Like.builder()
                    .user(user)
                    .boardPost(boardPost)
                    .build();
            likeRepository.save(like);
            boardPost.addLikeCount(1);
            return Response.success(null);
        }

        likeRepository.deleteByUserAndBoardPost(user, boardPost);
        boardPost.addLikeCount(-1);
        return Response.success(null);
    }

    /**
     * 학과 공지사항 목록 조회
     * @param collegeNoticesRequest
     * @return
     */
    public Response getCollegeNotices(CollegeNoticesRequest collegeNoticesRequest) {

        String direction = collegeNoticesRequest.getSort().getDirection();
        String columnName = collegeNoticesRequest.getSort().getColumnName();
        Sort sort = Sort.by(Sort.Direction.valueOf(direction), columnName);
        Pageable pageable = PageRequest.of(collegeNoticesRequest.getPageNumber() - 1, collegeNoticesRequest.getPageSize(), sort);

        Page<CollegeNoticesMapping> collegeNoticesMappingPage = noticeKnouOriginRepository.findAllByQuerydsl(pageable);

        return Response.success(new CollegeNoticesResponse(
                PagingResponse.createPagingInfo(collegeNoticesMappingPage), collegeNoticesMappingPage.getContent()
        ));
    }
}
