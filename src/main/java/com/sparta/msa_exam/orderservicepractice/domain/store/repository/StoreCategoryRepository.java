package com.sparta.msa_exam.orderservicepractice.domain.store.repository;

import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Store;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, UUID> {
    List<StoreCategory> findByStore(Store store);
}
