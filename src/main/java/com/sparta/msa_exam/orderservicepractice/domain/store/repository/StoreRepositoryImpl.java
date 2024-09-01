package com.sparta.msa_exam.orderservicepractice.domain.store.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.QStore;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class StoreRepositoryImpl implements StoreCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Store> findByKeyword(String keyword, Pageable pageable) {
        QStore store = QStore.store;

        List<Store> results = queryFactory
                .selectFrom(store)
                .where(store.name.containsIgnoreCase(keyword)) // name 필드에 대해 검색
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(store)
                .where(store.name.containsIgnoreCase(keyword))
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }
}