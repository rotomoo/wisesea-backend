package com.example.knu.domain.repository.custom;

import com.example.knu.domain.entity.*;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.board.QBoardPost;
import com.example.knu.dto.board.response.BoardPostListResponseDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.knu.domain.entity.QBoardPostHashtag.boardPostHashtag;
import static com.example.knu.domain.entity.QComment.comment;
import static com.example.knu.domain.entity.QFile.file;
import static com.example.knu.domain.entity.QHashtag.hashtag;
import static com.example.knu.domain.entity.QLike.like;
import static com.example.knu.domain.entity.board.QBoardCategory.boardCategory;
import static com.example.knu.domain.entity.board.QBoardPost.boardPost;
import static com.example.knu.domain.entity.user.QUser.user;

@Transactional
public class BoardPostCustomImpl implements BoardPostCustom {

    private final JPAQueryFactory queryFactory;

    public BoardPostCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void deleteByQuerydsl(BoardPost targetBoardPost) {
        queryFactory.delete(file)
                .where(file.boardPost.eq(targetBoardPost))
                .execute();

        queryFactory.delete(comment)
                .where(comment.boardPost.eq(targetBoardPost))
                .execute();

        queryFactory.delete(like)
                .where(like.boardPost.eq(targetBoardPost))
                .execute();

        queryFactory.delete(boardPostHashtag)
                .where(boardPostHashtag.boardPost.eq(boardPost))
                .execute();

        queryFactory.delete(boardPost)
                .where(boardPost.eq(targetBoardPost))
                .execute();
    }

    @Override
    public void deleteFileHashtagByQuerydsl(BoardPost targetBoardPost) {
        queryFactory.delete(file)
                .where(file.boardPost.eq(targetBoardPost))
                .execute();

        queryFactory.delete(boardPostHashtag)
                .where(boardPostHashtag.boardPost.eq(boardPost))
                .execute();
    }

    @Override
    public Page<BoardPostListResponseDto> findBoardPost(Long categoryId, Pageable pageable) {
        List<BoardPost> boardPosts = queryFactory
                .selectFrom(boardPost)
                .join(boardPost.user, user).fetchJoin()
                .leftJoin(boardPost.boardCategory, boardCategory)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(boardCategory.id.eq(categoryId))
                .fetch();

        List<BoardPostListResponseDto> boardPostListResponseDto = boardPosts.stream()
                .map(BoardPostListResponseDto::new)
                .collect(Collectors.toList());

        JPAQuery<Long> countQuery = queryFactory
                .select(boardPost.count())
                .from(boardPost)
                .where();
        return PageableExecutionUtils.getPage(boardPostListResponseDto, pageable, () -> countQuery.fetchOne());
    }
}
