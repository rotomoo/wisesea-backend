package com.example.knu.service;

import com.example.knu.common.Response;
import com.example.knu.common.s3.S3Directory;
import com.example.knu.common.s3.S3Uploader;
import com.example.knu.domain.entity.BoardPostHashtag;
import com.example.knu.domain.entity.File;
import com.example.knu.domain.entity.Hashtag;
import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import com.example.knu.domain.repository.*;
import com.example.knu.domain.repository.NoticeKnouOriginRepository;
import com.example.knu.dto.notice.NoticeCreation;
import com.example.knu.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            for (MultipartFile multipartFile : noticeCreation.getFiles()) {
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
}
