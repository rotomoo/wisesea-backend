package com.example.knu.domain.repository.custom;

import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.enums.UserType;
import com.example.knu.domain.mapping.BoardUnifiedPostMapping;
import com.example.knu.dto.board.response.BoardPostListResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.knu.domain.entity.QBoardPostHashtag.boardPostHashtag;
import static com.example.knu.domain.entity.QComment.comment;
import static com.example.knu.domain.entity.QFile.file;
import static com.example.knu.domain.entity.QLike.like;
import static com.example.knu.domain.entity.board.QBoard.board;
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

    @Override
    public Page<BoardUnifiedPostMapping> findAllByQuerydsl(Long boardId, Long categoryId, String input, PageRequest pageable) {
        QueryResults<BoardUnifiedPostMapping> results = queryFactory.select(Projections.fields(BoardUnifiedPostMapping.class,
                        board.id.as("boardId"),
                        board.name.as("boardName"),
                        boardCategory.id.as("boardCategoryId"),
                        boardCategory.name.as("boardCategoryName"),
                        boardPost.id.as("boardPostId"),
                        user.userId,
                        user.username,
                        user.userType,
                        user.nickname,
                        user.profileImageUrl,
                        boardPost.title,
                        boardPost.contents,
                        boardPost.viewCount,
                        boardPost.likeCount,
                        boardPost.createdAt,
                        boardPost.updatedAt
                ))
                .from(boardPost)
                .join(boardPost.boardCategory)
                .join(boardCategory.board)
                .join(boardPost.user)
                .where(generateWhereCondition(boardId, categoryId, input))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    /**
     * 동적 조회
     * @param categoryId
     * @param input
     * @return
     */
    private BooleanBuilder generateWhereCondition(Long boardId, Long categoryId, String input) {
        BooleanBuilder builder = new BooleanBuilder();

        // 보드 ID 필터
        if (boardId != null) {
            builder.and(board.id.eq(boardId));
        }

        // 카테고리 ID 필터
        if (categoryId != null) {
            builder.and(boardCategory.id.eq(categoryId));
        }

        // input 필터
        if (!StringUtils.isBlank(input)) {
            builder.or(board.name.contains(input.trim()));
            builder.or(boardCategory.name.contains(input.trim()));
            builder.or(user.username.contains(input.trim()));
            builder.or(user.nickname.contains(input.trim()));
            builder.or(boardPost.title.contains(input.trim()));
            builder.or(boardPost.contents.contains(input.trim()));
        }

        return builder;
    }

    /**
     * 동적 정렬
     * @param sort
     * @return
     */
    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        if (sort.isUnsorted()) {
            orders.add(new OrderSpecifier(Order.DESC, Expressions.path(BoardPost.class, boardPost, "id")));
        } else {
            // 동적 정렬 생성
            sort.stream().forEach(order -> {
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                String fieldName = order.getProperty();

                Path<Object> fieldPath = Expressions.path(BoardPost.class, boardPost, fieldName);

                orders.add(new OrderSpecifier(direction, fieldPath));
                orders.add(new OrderSpecifier(Order.DESC, boardPost.id));
            });
        }

        return orders;
    }
}
