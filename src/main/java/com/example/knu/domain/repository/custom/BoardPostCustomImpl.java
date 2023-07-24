package com.example.knu.domain.repository.custom;

import com.example.knu.domain.entity.*;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.board.QBoardPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import static com.example.knu.domain.entity.QBoardPostHashtag.boardPostHashtag;
import static com.example.knu.domain.entity.QComment.comment;
import static com.example.knu.domain.entity.QFile.file;
import static com.example.knu.domain.entity.QHashtag.hashtag;
import static com.example.knu.domain.entity.QLike.like;
import static com.example.knu.domain.entity.board.QBoardPost.boardPost;

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
}
