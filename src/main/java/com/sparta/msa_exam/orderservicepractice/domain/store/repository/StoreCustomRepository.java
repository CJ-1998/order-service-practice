package com.sparta.msa_exam.orderservicepractice.domain.store.repository;

import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreCustomRepository {
    Page<Store> findByKeyword(String keyword, Pageable pageable);
}
