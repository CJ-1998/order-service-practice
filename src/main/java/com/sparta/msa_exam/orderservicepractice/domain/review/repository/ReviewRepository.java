package com.sparta.msa_exam.orderservicepractice.domain.review.repository;

import com.sparta.msa_exam.orderservicepractice.domain.review.domain.Review;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByStoreId(UUID storeId);
    List<Review> findByStoreIdAndReportedFalse(UUID storeId);

    Page<Review> findByStoreId(UUID storeId, Pageable pageable);
    Page<Review> findByStoreIdAndReportedFalse(UUID storeId, Pageable pageable);
}
