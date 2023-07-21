package com.example.knu.service.notice;

import com.example.knu.common.Response;
import com.example.knu.common.s3.S3Directory;
import com.example.knu.common.s3.S3Uploader;
import com.example.knu.domain.entity.File;
import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import com.example.knu.domain.repository.BoardCategoryRepository;
import com.example.knu.domain.repository.BoardPostRepository;
import com.example.knu.domain.repository.FileRepository;
import com.example.knu.domain.repository.notice.NoticeKnouOriginRepository;
import com.example.knu.dto.notice.NoticeCreation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeKnouOriginRepository noticeKnouOriginRepository;

    private final BoardPostRepository boardPostRepository;

    private final BoardCategoryRepository boardCategoryRepository;

    private final S3Uploader s3Uploader;

    private final FileRepository fileRepository;

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
}
