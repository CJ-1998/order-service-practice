package com.sparta.msa_exam.orderservicepractice.domain.review.service;

import com.sparta.msa_exam.orderservicepractice.domain.review.domain.Review;
import com.sparta.msa_exam.orderservicepractice.domain.review.repository.ReviewRepository;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Store;
import com.sparta.msa_exam.orderservicepractice.domain.store.repository.StoreRepository;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import com.sparta.msa_exam.orderservicepractice.domain.user.repository.UserRepository;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ServiceException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Review addReview(UUID storeId, Long userId, int rating, String comment) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        Review review = Review.builder()
                .store(store)
                .user(user)
                .rating(rating)
                .comment(comment)
                .build();

        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public Review getReviewById(UUID reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<Review> getReviewsByStore(UUID storeId, boolean includeReported, Pageable pageable) {
        if (includeReported) {
            return reviewRepository.findByStoreId(storeId, pageable);
        } else {
            return reviewRepository.findByStoreIdAndReportedFalse(storeId, pageable);
        }
    }

    @Transactional
    public void reportReview(UUID reviewId, String reportMessage) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));
        review.updateReport(reportMessage);
        reviewRepository.save(review);
    }
}
