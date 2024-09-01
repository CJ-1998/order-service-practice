package com.sparta.msa_exam.orderservicepractice.domain.store.repository;

import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID>, StoreCustomRepository {
}
