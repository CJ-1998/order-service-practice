package com.sparta.msa_exam.orderservicepractice.domain.review.domain.mapper;

import com.sparta.msa_exam.orderservicepractice.domain.review.domain.Review;
import com.sparta.msa_exam.orderservicepractice.domain.review.domain.dtos.ReviewRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.review.domain.dtos.ReviewResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.Store;
import com.sparta.msa_exam.orderservicepractice.domain.store.repository.StoreRepository;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import com.sparta.msa_exam.orderservicepractice.domain.user.repository.UserRepository;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ErrorCode;
import com.sparta.msa_exam.orderservicepractice.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public ReviewResponseDto toReviewResponseDto(Review review) {
        if (review == null) {
            throw new ServiceException(ErrorCode.NULL_OR_EMPTY_VALUE);
        }

        return ReviewResponseDto.builder()
                .id(review.getId())
                .storeId(review.getStore().getId())
                .userId(review.getUser().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .reported(review.isReported())
                .reportMessage(review.getReportMessage())
                .build();
    }

    public Review toReview(ReviewRequestDto dto) {
        if (dto == null) {
            throw new ServiceException(ErrorCode.NULL_OR_EMPTY_VALUE);
        }

        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND));

        return Review.builder()
                .store(store)
                .user(user)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();
    }
}
