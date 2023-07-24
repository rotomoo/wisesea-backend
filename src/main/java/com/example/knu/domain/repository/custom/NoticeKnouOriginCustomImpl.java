package com.example.knu.domain.repository.custom;

import com.example.knu.domain.entity.NoticeKnouOrigin;
import com.example.knu.domain.entity.QNoticeKnouOrigin;
import com.example.knu.domain.mapping.CollegeNoticesMapping;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.example.knu.domain.entity.QNoticeKnouOrigin.noticeKnouOrigin;

public class NoticeKnouOriginCustomImpl implements NoticeKnouOriginCustom {

    private final JPAQueryFactory queryFactory;

    public NoticeKnouOriginCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<CollegeNoticesMapping> findAllByQuerydsl(Pageable pageable) {
        QueryResults<CollegeNoticesMapping> results = queryFactory.select(Projections.fields(CollegeNoticesMapping.class,
                        noticeKnouOrigin.id,
                        noticeKnouOrigin.code,
                        noticeKnouOrigin.title,
                        noticeKnouOrigin.write,
                        noticeKnouOrigin.date,
                        noticeKnouOrigin.view,
                        noticeKnouOrigin.url
                ))
                .from(noticeKnouOrigin)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    /**
     * 동적 정렬
     * @param sort
     * @return
     */
    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        if (sort.isUnsorted()) {
            orders.add(new OrderSpecifier(Order.DESC, Expressions.path(NoticeKnouOrigin.class, noticeKnouOrigin, "date")));
        } else {
            // 동적 정렬 생성
            sort.stream().forEach(order -> {
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                String fieldName = order.getProperty();

                Path<Object> fieldPath = Expressions.path(NoticeKnouOrigin.class, noticeKnouOrigin, fieldName);

                orders.add(new OrderSpecifier(direction, fieldPath));
                orders.add(new OrderSpecifier(Order.DESC, noticeKnouOrigin.date));
            });
        }

        return orders;
    }
}
