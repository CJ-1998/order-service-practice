package com.sparta.msa_exam.orderservicepractice.domain.store.repository;

import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Category;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByName(@NotBlank String name);

    Optional<Category> findByName(String categoryName);
}
